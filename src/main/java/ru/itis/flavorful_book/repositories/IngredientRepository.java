package ru.itis.flavorful_book.repositories;

import ru.itis.flavorful_book.models.Ingredient;

import java.util.List;

public interface IngredientRepository {
    Ingredient findById(Long id);

    List<Ingredient> findAll();

    List<Ingredient> findAllByRecipeId(Long recipeId);

    boolean exists(Long id);
}
