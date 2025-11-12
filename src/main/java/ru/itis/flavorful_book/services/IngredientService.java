package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.models.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient findById(Long id);

    List<Ingredient> findAll();

    List<Ingredient> findAllByRecipeId(Long recipeId);
}
