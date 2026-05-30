package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Recipe;

import java.util.List;

public interface RecipeRepository {
    Recipe save(Recipe recipe);

    boolean update(Recipe recipe);

    void updateViews(Long id);

    boolean deleteById(Long id);

    Recipe findById(Long id);

    List<Recipe> findAll();

    List<Recipe> findAllByUserFavorites(Long userId);

    List<Recipe> findAllByUserId(Long userId);

    List<Recipe> findAll(List<Long> ingredients, boolean isStrictMatchIngredient,
                         List<Long> categories, boolean isStrictMatchCategories,
                         int activeStart, int activeEnd, int totalStart, int totalEnd);

    boolean existsById(Long id);

    boolean addToFavorites(Long userId, Long recipeId);

    boolean deleteFromFavorites(Long userId, Long recipeId);

    boolean isInFavorites(Long userId, Long recipeId);
}
