package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.entity.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient findById(Long id);

    List<Ingredient> findAll();

    List<Ingredient> findAllByRecipeId(Long recipeId);
}
