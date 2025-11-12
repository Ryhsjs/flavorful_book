package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserRepositoryJDBC implements UserRepository {
    private final DataSource dataSource;

    private final String SAVE_QUERY = """
            INSERT INTO users (username, email, password_hash, salt, avatar_url)
            VALUES (?, ?, ?, ?, ?)
            RETURNING user_id, created_at
            """;

    private final String UPDATE_QUERY = """
            UPDATE users
            SET username = ?,
                avatar_url = ?
            WHERE user_id = ?
            """;

    private final String FIND_BY_ID_QUERY = """
            SELECT user_id,
                   username,
                   email,
                   password_hash,
                   salt,
                   avatar_url,
                   created_at
            FROM users
            WHERE user_id = ?;
            """;

    private final String FIND_BY_EMAIL_QUERY = """
            SELECT user_id,
                   username,
                   email,
                   password_hash,
                   salt,
                   avatar_url,
                   created_at
            FROM users
            WHERE email = ?;
            """;

    private final String FIND_BY_USERNAME_QUERY = """
            SELECT user_id,
                   username,
                   email,
                   password_hash,
                   salt,
                   avatar_url,
                   created_at
            FROM users
            WHERE username = ?;
            """;

    private final String EXISTS_BY_ID_QUERY = """
            SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)
            """;

    private final String EMAIL_EXISTS_QUERY = """
            SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)
            """;

    private final String USERNAME_EXISTS_QUERY = """
            SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)
            """;

    private final String COUNT_BY_FAVORITES_QUERY = """
            SELECT COUNT(*)
            FROM favorites
            WHERE recipe_id = ?
            GROUP BY recipe_id;
            """;

    public UserRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User save(User user) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_QUERY)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getSalt());
            statement.setString(5, user.getAvatarUrl());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                    user.setCreatedAt(resultSet.getTimestamp(2).toLocalDateTime());
                    return user;
                }
                throw new RuntimeException("Не удалось создать пользователя");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(User user) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getAvatarUrl());
            statement.setLong(3, user.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(Long id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return toUser(resultSet);
                }
                throw new RuntimeException("Пользователь не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByEmail(String email) {
        return findByQuery(FIND_BY_EMAIL_QUERY, email);
    }

    @Override
    public User findByUsername(String username) {
        return findByQuery(FIND_BY_USERNAME_QUERY, username);
    }

    private User findByQuery(String query, String param) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
            statement.setString(1, param);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return toUser(resultSet);
                }
                throw new RuntimeException("Пользователь не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public boolean existsByEmail(String email) {
        return existByQuery(EMAIL_EXISTS_QUERY, email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return existByQuery(USERNAME_EXISTS_QUERY, username);
    }

    private boolean existByQuery(String query, String param) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
            statement.setString(1, param);
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
    public int countByFavorites(Long recipeId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(COUNT_BY_FAVORITES_QUERY)) {
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

    private User toUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password_hash"),
                resultSet.getString("salt"),
                resultSet.getString("avatar_url"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
