package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.User;

public interface UserRepository {
    User save(User user);

    boolean update(User user);

    User findById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    int countByFavorites(Long recipeId);
}
