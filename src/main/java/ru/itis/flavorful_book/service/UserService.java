package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.entity.User;

public interface UserService {
    boolean update(Long id,String username, String avatarUrl);

    User findById(Long id);
}
