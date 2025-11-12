package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Category;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryJDBC implements CategoryRepository {
    private final DataSource dataSource;

    private final String SAVE_BY_RECIPE_ID_QUERY = """
            INSERT INTO recipe_categories (recipe_id, category_id)
            VALUES (?, ?);
            """;

    private final String DELETE_BY_RECIPE_ID_QUERY = """
            DELETE
            FROM recipe_categories
            WHERE recipe_id = ?
            AND category_id = ?;
            """;

    private final String FIND_BY_ID_QUERY = """
            SELECT category_id,
                   name,
                   parent_category_id,
                   description
            FROM categories
            WHERE category_id = ?;
            """;

    private final String FIND_ALL_QUERY = """
            SELECT category_id,
                   name,
                   parent_category_id,
                   description
            FROM categories
            ORDER BY name;
            """;

    private final String FIND_ALL_BY_PARENT_ID_QUERY = """
            SELECT category_id,
                   name,
                   parent_category_id,
                   description
            FROM categories
            WHERE parent_id = ?;
            """;

    private final String FIND_ALL_BY_RECIPE_ID_QUERY = """
            SELECT c.category_id,
                   name,
                   parent_category_id,
                   description
            FROM categories c
                     INNER JOIN recipe_categories rc
                                ON c.category_id = rc.category_id
            WHERE recipe_id = ?
            """;

    private final String EXISTS_QUERY = """
            SELECT EXISTS (SELECT 1 FROM categories WHERE category_id = ?);
            """;

    public CategoryRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(Long recipeId, Long categoryId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_BY_RECIPE_ID_QUERY)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, categoryId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long recipeId, Long categoryId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(DELETE_BY_RECIPE_ID_QUERY)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, categoryId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category findById(Long categoryId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)
        ) {
            statement.setLong(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return toCategory(resultSet);
                }
                throw new RuntimeException("Категория не найдена");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
                categories.add(toCategory(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public List<Category> findAllByParentId(Long parentId) {
        return findAllByQuery(FIND_ALL_BY_PARENT_ID_QUERY, parentId);
    }

    @Override
    public List<Category> findAllByRecipeId(Long recipeId) {
        return findAllByQuery(FIND_ALL_BY_RECIPE_ID_QUERY, recipeId);
    }

    @Override
    public boolean exists(Long categoryId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)
        ) {
            statement.setLong(1, categoryId);

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
    public boolean exists(Long recipeId, Long categoryId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(EXISTS_QUERY)) {
            statement.setLong(1, categoryId);

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

    private List<Category> findAllByQuery(String query, Long id) {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    categories.add(toCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    private Category toCategory(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getLong("category_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getLong("parent_category_id")
        );
    }
}
