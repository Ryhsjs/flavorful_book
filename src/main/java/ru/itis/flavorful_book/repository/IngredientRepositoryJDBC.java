package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Ingredient;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepositoryJDBC implements IngredientRepository {
    private final DataSource dataSource;

    private final String FIND_BY_ID_QUERY = """
            SELECT ingredient_id,
                   name
            FROM ingredients
            WHERE ingredient_id = ?
            """;

    private final String FIND_ALL_QUERY = """
            SELECT ingredient_id,
                   name
            FROM ingredients
            ORDER BY name;
            """;

    private final String FIND_ALL_BY_RECIPE_ID_QUERY = """
            SELECT i.ingredient_id,
                   name
            FROM ingredients i
                     RIGHT JOIN recipe_ingredients ri
                                ON i.ingredient_id = ri.ingredient_id
            WHERE recipe_id = ?;
            """;

        private final String EXISTS_QUERY = """
            SELECT EXISTS (SELECT 1 FROM ingredients WHERE ingredient_id = ?);
            """;

    public IngredientRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Ingredient findById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return toIngredient(resultSet);
                }
                throw new RuntimeException("Ингредиент не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
                ingredients.add(toIngredient(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

    @Override
    public List<Ingredient> findAllByRecipeId(Long recipeId) {
        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_BY_RECIPE_ID_QUERY)
        ) {
            statement.setLong(1, recipeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    ingredients.add(toIngredient(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

    @Override
    public boolean exists(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(EXISTS_QUERY)
        ) {
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

    private Ingredient toIngredient(ResultSet resultSet) throws SQLException {
        return new Ingredient(
                resultSet.getLong("ingredient_id"),
                resultSet.getString("name")
        );
    }
}
