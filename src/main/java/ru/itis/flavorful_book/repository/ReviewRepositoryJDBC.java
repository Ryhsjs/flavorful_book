package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Review;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryJDBC implements ReviewRepository {
    private final DataSource dataSource;

    private final String SAVE_QUERY = """
            INSERT INTO reviews(user_id, recipe_id, rating, comment)
            VALUES (?, ?, ?, ?)
            RETURNING review_id, created_at
            """;

    private final String UPDATE_QUERY = """
            UPDATE reviews
            SET rating = ?,
                comment = ?,
                updated_at = ?
            WHERE review_id = ?
            """;

    private final String DELETE_BY_ID_QUERY = """
            DELETE FROM reviews
            WHERE review_id = ?
            """;

    private final String FIND_BY_ID_QUERY = """
            SELECT review_id,
                   recipe_id,
                   user_id,
                   rating,
                   comment,
                   created_at,
                   updated_at
            FROM reviews
            WHERE review_id = ?
            """;

    private final String FIND_BY_USER_ID_RECIPE_ID_QUERY = """
            SELECT review_id,
                   recipe_id,
                   user_id,
                   rating,
                   comment,
                   created_at,
                   updated_at
            FROM reviews
            WHERE user_id = ? AND recipe_id = ?;
            """;

    private final String FIND_ALL_BY_RECIPE_ID_QUERY = """
            SELECT review_id,
                   recipe_id,
                   user_id,
                   rating,
                   comment,
                   created_at,
                   updated_at
            FROM reviews WHERE recipe_id = ?;
            """;

    private final String EXISTS_BY_USER_ID_RECIPE_ID_QUERY = """
            SELECT EXISTS(SELECT 1 FROM reviews WHERE user_id = ? AND recipe_id = ?)
            """;

    private final String EXISTS_BY_ID_QUERY = """
            SELECT EXISTS(SELECT 1 FROM reviews WHERE review_id = ?)
            """;

    private final String COUNT_BY_RECIPE_ID_QUERY = """
            SELECT COUNT(*)
            FROM reviews
            WHERE recipe_id = ?
            GROUP BY recipe_id;
            """;

    public ReviewRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Review save(Review review) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_QUERY)) {
            statement.setLong(1, review.getUserId());
            statement.setLong(2, review.getRecipeId());
            statement.setLong(3, review.getRating());
            statement.setString(4, review.getComment());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    review.setId(resultSet.getLong(1));
                    review.setCreatedAt(resultSet.getTimestamp(2).toLocalDateTime());
                    return review;
                }
                throw new RuntimeException("Не удалось сохранить отзыв");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Review review) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, review.getRating());
            statement.setString(2, review.getComment());
            statement.setTimestamp(3, Timestamp.valueOf(review.getUpdatedAt()));
            statement.setLong(4, review.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public Review findById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return toReview(resultSet);
                throw new RuntimeException("Отзыв не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Review findByUserIdRecipeId(Long userId, Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_USER_ID_RECIPE_ID_QUERY)) {
            statement.setLong(1, userId);
            statement.setLong(2, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return toReview(resultSet);
                throw new RuntimeException("Отзыв не найде");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Review> findAllByRecipeId(Long recipeId) {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_ALL_BY_RECIPE_ID_QUERY)) {
            statement.setLong(1, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    reviews.add(toReview(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reviews;
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
    public boolean existsByUserIdRecipeId(Long userId, Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(EXISTS_BY_USER_ID_RECIPE_ID_QUERY)) {
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

    @Override
    public int countByRecipeId(Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(COUNT_BY_RECIPE_ID_QUERY)) {
            statement.setLong(1, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Review toReview(ResultSet resultSet) throws SQLException {
        return new Review(
                resultSet.getLong("review_id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("recipe_id"),
                resultSet.getInt("rating"),
                resultSet.getString("comment"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getTimestamp("updated_at").toLocalDateTime()
        );
    }
}
