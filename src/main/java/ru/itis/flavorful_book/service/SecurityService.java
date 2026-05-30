package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.entity.User;

public interface SecurityService {
    String signupUser(String username, String email, String password, String passwordRepeat);

    String loginUser(String email, String password);

    User getUser(String sessionId);

    boolean logoutUser(String sessionId);
}
