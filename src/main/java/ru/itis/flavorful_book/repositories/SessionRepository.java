package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Session;

public interface SessionRepository {
    boolean save(Session session);

    Session findById(String id);

    boolean deleteBySessionId(String sessionId);
}
