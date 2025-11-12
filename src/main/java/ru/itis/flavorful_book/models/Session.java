package ru.itis.flavorful_book.models;

import java.time.LocalDateTime;

public class Session {

    private final String sessionId;

    private final Long userId;

    private final LocalDateTime expiresAt;

    public Session(String sessionId, Long userId, LocalDateTime expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
