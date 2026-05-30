package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Session;

public interface SessionRepository {
    boolean save(Session session);

    Session findById(String id);

    boolean deleteBySessionId(String sessionId);
}
