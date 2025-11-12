package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.models.User;

public interface UserService {
    boolean update(Long id,String username, String avatarUrl);

    User findById(Long id);
}
