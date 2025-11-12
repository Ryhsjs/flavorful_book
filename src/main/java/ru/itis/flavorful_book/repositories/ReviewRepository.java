package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Review;

import java.util.List;

public interface ReviewRepository {
    Review save(Review review);

    boolean update(Review review);

    boolean deleteById(Long id);

    Review findById(Long id);

    Review findByUserIdRecipeId(Long userId, Long recipeId);

    List<Review> findAllByRecipeId(Long recipeId);

    boolean existsById(Long id);

    boolean existsByUserIdRecipeId(Long userId, Long recipeId);

    int countByRecipeId(Long recipeId);
}
