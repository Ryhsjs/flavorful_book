package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.enums.Unit;

import java.util.List;

public interface IngredientRecipeRepository {
    boolean save(Long recipeId, Long ingredientId, Integer quantity, Unit unit, String notes);

    boolean update(Long recipeId, Long ingredientId, Integer quantity, Unit unit, String notes);

    boolean delete(Long recipeId, Long ingredientId);

    boolean exists(Long recipeId, Long ingredientId);

    List<IngredientRecipe> findAll(Long recipeId);
}
