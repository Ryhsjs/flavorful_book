package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.enums.Unit;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientRecipeRepositoryJDBC implements IngredientRecipeRepository {
    private final DataSource dataSource;

    public IngredientRecipeRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final String SAVE_QUERY = """
            INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, notes)
            VALUES (?, ?, ?, ?, ?);
            """;

    private final String UPDATE_QUERY = """
            UPDATE recipe_ingredients
            SET quantity = ?,
                unit     = ?,
                notes    = ?
            WHERE recipe_id = ?
              AND ingredient_id = ?;
            """;

    private final String DELETE_QUERY = """
            DELETE FROM recipe_ingredients
            WHERE recipe_id = ? AND ingredient_id = ?;
            """;

    private final String EXISTS_QUERY = """
            SELECT EXISTS (SELECT 1 FROM recipe_ingredients WHERE recipe_id = ? AND ingredient_id = ?);
            """;

    private final String FIND_ALL_QUERY = """
            SELECT i.ingredient_id,
                   name,
                   recipe_id,
                   quantity,
                   unit,
                   notes
            FROM ingredients i
                     INNER JOIN recipe_ingredients ri
                                ON i.ingredient_id = ri.ingredient_id
            WHERE recipe_id = ?;
            """;

    @Override
    public boolean save(Long recipeId, Long ingredientId, Integer quantity, Unit unit, String notes) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_QUERY)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, ingredientId);
            statement.setInt(3, quantity);
            statement.setString(4, unit.toString());
            statement.setString(5, notes);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Long recipeId, Long ingredientId, Integer quantity, Unit unit, String notes) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, quantity);
            statement.setString(2, unit.toString());
            statement.setString(3, notes);
            statement.setLong(4, recipeId);
            statement.setLong(5, ingredientId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long recipeId, Long ingredientId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(DELETE_QUERY)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, ingredientId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(Long recipeId, Long ingredientId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(EXISTS_QUERY)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, ingredientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getBoolean(1);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IngredientRecipe> findAll(Long recipeId) {
        List<IngredientRecipe> ingredients = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_QUERY)
        ) {
            statement.setLong(1, recipeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    ingredients.add(toIngredientRecipe(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredients;
    }

    private IngredientRecipe toIngredientRecipe(ResultSet resultSet) throws SQLException {
        return new IngredientRecipe(
                resultSet.getLong("ingredient_id"),
                resultSet.getString("name"),
                resultSet.getLong("recipe_id"),
                resultSet.getInt("quantity"),
                Unit.valueOf(resultSet.getString("unit")),
                resultSet.getString("notes")
        );
    }
}
