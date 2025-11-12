package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.models.User;

public interface SecurityService {
    String signupUser(String username, String email, String password, String passwordRepeat);

    String loginUser(String email, String password);

    User getUser(String sessionId);

    boolean logoutUser(String sessionId);
}
