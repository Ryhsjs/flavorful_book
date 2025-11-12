package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.models.Ingredient;
import ru.itis.flavorful_book.repositories.IngredientRepository;

import java.util.List;

public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient findById(Long id) {
        if (!ingredientRepository.exists(id))
            throw new IllegalArgumentException("Такого ингредиента не существует");
        return ingredientRepository.findById(id);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public List<Ingredient> findAllByRecipeId(Long recipeId) {
        return ingredientRepository.findAllByRecipeId(recipeId);
    }
}
