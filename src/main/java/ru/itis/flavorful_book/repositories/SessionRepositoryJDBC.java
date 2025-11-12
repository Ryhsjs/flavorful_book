package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Session;

import javax.sql.DataSource;
import java.lang.RuntimeException;
import java.sql.*;

public class SessionRepositoryJDBC implements SessionRepository {
    private final DataSource dataSource;

    private final String SAVE_QUERY = """
            INSERT INTO sessions (session_id, user_id, expire_at)
            VALUES (?, ?, ?)
            """;

    private final String FIND_BY_ID = """
            SELECT session_id,
                   user_id,
                   expire_at
            FROM sessions
            WHERE session_id = ?
            """;

    private final String DELETE_BY_SESSION_ID_QUERY = """
            DELETE FROM sessions
            WHERE session_id = ?
            """;

    public SessionRepositoryJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean save(Session session) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SAVE_QUERY)
        ){
            statement.setString(1, session.getSessionId());
            statement.setLong(2, session.getUserId());
            statement.setTimestamp(3, Timestamp.valueOf(session.getExpiresAt()));

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Session findById(String id) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(FIND_BY_ID)
        ) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return toSession(resultSet);
                }
                throw new RuntimeException("Сессия не найдена");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteBySessionId(String sessionId) {
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(DELETE_BY_SESSION_ID_QUERY)) {
            statement.setString(1, sessionId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Session toSession(ResultSet resultSet) throws SQLException{
        return new Session(
                resultSet.getString("session_id"),
                resultSet.getLong("user_id"),
                resultSet.getTimestamp("expire_at").toLocalDateTime()
        );
    }
}
