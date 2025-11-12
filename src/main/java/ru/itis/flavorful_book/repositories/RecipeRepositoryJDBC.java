package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Recipe;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepositoryJDBC implements RecipeRepository {
    private final DataSource dataSource;

    private final String SAVE_QUERY = """
            INSERT INTO recipes(title, description, instructions, active_cooking_time, total_cooking_time,
                                servings, image_url, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING recipe_id, created_at;
            """;

    private final String UPDATE_QUERY = """
            UPDATE recipes
            SET title               = ?,
                description         = ?,
                instructions        = ?,
                active_cooking_time = ?,
                total_cooking_time  = ?,
                servings            = ?,
                image_url           = ?,
                updated_at          = NOW()
            WHERE recipe_id = ?;
            """;

    private final String UPDATE_VIEWS_QUERY = """
            UPDATE recipes
            SET views = views + 1
            WHERE recipe_id = ?;
            """;

    private final String DELETE_BY_ID_QUERY = """
            DELETE FROM recipes
            WHERE recipe_id = ?;
            """;

    private final String FIND_BY_ID_QUERY = """
            SELECT recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   user_id,
                   views,
                   rating
            FROM recipes
            WHERE recipe_id = ?;
            """;

    private final String FIND_ALL_QUERY = """
            SELECT recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   user_id,
                   views,
                   rating
            FROM recipes
            ORDER BY created_at DESC;
            """;

    private final String FIND_ALL_BY_FAVORITES = """
            SELECT r.recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   r.user_id,
                   views,
                   rating
            FROM recipes r
                     INNER JOIN favorites f
                         ON r.recipe_id = f.recipe_id
            WHERE f.user_id = ?
            ORDER BY saved_at DESC;
            """;


    private final String FIND_ALL_BY_USER_ID_QUERY = """
             SELECT recipe_id,
                    title,
                    description,
                    instructions,
                    active_cooking_time,
                    total_cooking_time,
                    servings,
                    image_url,
                    created_at,
                    updated_at,
                    user_id,
                    views,
                    rating
             FROM recipes
             WHERE user_id = ?
            ORDER BY created_at DESC;
            """;

    private final String FIND_MANY_BY_CATEGORY_ID_QUERY = """
            SELECT r.recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   user_id,
                   views,
                   rating
            FROM recipes r
                     INNER JOIN recipe_categories rc
                               ON r.recipe_id = rc.recipe_id
            WHERE category_id = ?
            """;

    private final String FIND_MANY_BY_INGREDIENT_ID_QUERY = """
            SELECT r.recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   user_id,
                   views,
                   rating
            FROM recipes r
                     INNER JOIN recipe_ingredients ri
                               ON r.recipe_id = ri.recipe_id
            WHERE ingredient_id = ?
            """;

    private final String FIND_BY_COOKING_TIME_QUERY = """
            (SELECT recipe_id,
                   title,
                   description,
                   instructions,
                   active_cooking_time,
                   total_cooking_time,
                   servings,
                   image_url,
                   created_at,
                   updated_at,
                   user_id,
                   views,
                   rating
            FROM recipes r
            WHERE active_cooking_time >= ? and active_cooking_time <= ? and
                  total_cooking_time >= ? and total_cooking_time <= ?)
            """;


    private final String EXISTS_BY_ID_QUERY = """
            SELECT EXISTS (SELECT 1 FROM recipes WHERE recipe_id = ?);
            """;

    private final String ADD_TO_FAVORITES_QUERY = """
            INSERT INTO favorites (user_id, recipe_id)
            VALUES(?, ?);
            """;

    private final String DELETE_FROM_FAVORITES_QUERY = """
            DELETE FROM favorites
            WHERE user_id = ? AND recipe_id = ?;
            """;

    private final String IS_IN_FAVORITES_QUERY = """
            SELECT EXISTS (SELECT 1 FROM favorites WHERE user_id = ? AND recipe_id = ?);
            """;

    private final String INTERSECT = "INTERSECT\n";

    private final String UNION = "UNION\n";

    private final String QUERY_START = "SELECT * FROM (\n";

    private final String QUERY_END = ") as temp\n";

    private final String ORDER_BY = """
            ORDER BY created_at DESC;
            """;

    public RecipeRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Recipe save(Recipe recipe) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_QUERY)) {
            prepareStatement(recipe, statement);
            statement.setLong(8, recipe.getUserId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    recipe.setId(resultSet.getLong(1));
                    recipe.setCreatedAt(resultSet.getTimestamp(2).toLocalDateTime());
                    return recipe;
                }
                throw new RuntimeException("Не получилось сохранить рецепт");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Recipe recipe) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE_QUERY)) {
            prepareStatement(recipe, statement);
            statement.setLong(8, recipe.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateViews(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE_VIEWS_QUERY)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareStatement(Recipe recipe, PreparedStatement statement) throws SQLException {
        statement.setString(1, recipe.getTitle());
        statement.setString(2, recipe.getDescription());
        statement.setString(3, recipe.getInstructions());
        statement.setInt(4, recipe.getActiveCookingTime());
        statement.setInt(5, recipe.getTotalCookingTime());
        statement.setInt(6, recipe.getServings());
        statement.setString(7, recipe.getImageUrl());
    }

    @Override
    public boolean deleteById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(DELETE_BY_ID_QUERY)) {
            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Recipe findById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return toRecipe(resultSet);
                throw new RuntimeException("Рецепт не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next())
                recipes.add(toRecipe(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recipes;
    }

    @Override
    public List<Recipe> findAllByUserFavorites(Long userId) {
        return findManyByQuery(FIND_ALL_BY_FAVORITES, userId);
    }

    @Override
    public List<Recipe> findAllByUserId(Long userId) {
        return findManyByQuery(FIND_ALL_BY_USER_ID_QUERY, userId);
    }

    private List<Recipe> findManyByQuery(String query, Long userId) {
        List<Recipe> recipes = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection()
                .prepareStatement(query)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) recipes.add(toRecipe(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recipes;
    }

    @Override
    public List<Recipe> findAll(List<Long> ingredients, boolean isStrictMatchIngredient,
                                List<Long> categories, boolean isStrictMatchCategories,
                                int activeStart, int activeEnd, int totalStart, int totalEnd
    ) {
        String queriesIngredients = prepareQuery(FIND_MANY_BY_INGREDIENT_ID_QUERY, isStrictMatchIngredient, ingredients.size());
        String queriesCategories = prepareQuery(FIND_MANY_BY_CATEGORY_ID_QUERY, isStrictMatchCategories, categories.size());

        String queries = QUERY_START + FIND_BY_COOKING_TIME_QUERY;

        if (queriesIngredients != null) {
            queries += INTERSECT + queriesIngredients;
        }

        if (queriesCategories != null) {
            queries += INTERSECT + queriesCategories;
        }
        queries += QUERY_END + ORDER_BY;

        List<Recipe> recipes = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(queries)) {
            prepareStatement(activeStart, activeEnd, totalStart, totalEnd, ingredients, categories, statement);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) recipes.add(toRecipe(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recipes;
    }

    @Override
    public boolean existsById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(EXISTS_BY_ID_QUERY)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addToFavorites(Long userId, Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(ADD_TO_FAVORITES_QUERY)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteFromFavorites(Long userId, Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(DELETE_FROM_FAVORITES_QUERY)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isInFavorites(Long userId, Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(IS_IN_FAVORITES_QUERY)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String prepareQuery(String query, boolean isStrictMatch, int count) {
        if (count == 0)
            return null;

        String separator;
        if (isStrictMatch)
            separator = INTERSECT;
        else
            separator = UNION;

        StringBuilder queryBuilder = new StringBuilder("(").append(query);
        for (int i = 1; i < count; i++) {
            queryBuilder.append(separator);
            queryBuilder.append(query);
        }
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    private void prepareStatement(int activeStart, int activeEnd, int totalStart, int totalEnd,
                                  List<Long> list1, List<Long> list2, PreparedStatement statement) throws SQLException {
        statement.setInt(1, activeStart);
        statement.setInt(2, activeEnd);
        statement.setInt(3, totalStart);
        statement.setInt(4, totalEnd);

        List<Long> params = new ArrayList<>(list1);
        params.addAll(list2);

        int paramsSize = params.size();

        for (int i = 0; i < paramsSize; i++) {
            statement.setLong(i + 5, params.get(i));
        }
    }

    private Recipe toRecipe(ResultSet resultSet) throws SQLException {
        return new Recipe(
                resultSet.getLong("recipe_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("instructions"),
                resultSet.getInt("active_cooking_time"),
                resultSet.getInt("total_cooking_time"),
                resultSet.getInt("servings"),
                resultSet.getString("image_url"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                (resultSet.getTimestamp("updated_at") == null ? null :
                        resultSet.getTimestamp("updated_at").toLocalDateTime()),
                resultSet.getLong("user_id"),
                resultSet.getInt("views"),
                resultSet.getFloat("rating"));
    }
}
