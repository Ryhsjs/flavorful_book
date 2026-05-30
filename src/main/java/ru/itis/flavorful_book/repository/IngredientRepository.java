package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Ingredient;

import java.util.List;

public interface IngredientRepository {
    Ingredient findById(Long id);

    List<Ingredient> findAll();

    List<Ingredient> findAllByRecipeId(Long recipeId);

    boolean exists(Long id);
}
