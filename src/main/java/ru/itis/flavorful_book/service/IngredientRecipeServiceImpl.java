package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.DTO.IngredientDTO;
import ru.itis.flavorful_book.entity.Ingredient;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.enums.Unit;
import ru.itis.flavorful_book.repository.IngredientRecipeRepository;
import ru.itis.flavorful_book.util.validation.Validator;

import java.util.List;

public class IngredientRecipeServiceImpl implements IngredientRecipeService {
    private final IngredientRecipeRepository ingredientRecipeRepository;

    private final Validator validator;

    public IngredientRecipeServiceImpl(
            IngredientRecipeRepository ingredientRecipeRepository,
            Validator validator
    ) {
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.validator = validator;
    }

    @Override
    public void save(Long recipeId, IngredientDTO ingredient) {
        try {
            Unit unit = Unit.valueOf(ingredient.unit());
            if (ingredientRecipeRepository.exists(recipeId, ingredient.id()))
                ingredientRecipeRepository.update(recipeId, ingredient.id(), ingredient.quantity(), unit, ingredient.notes());
            else
                ingredientRecipeRepository.save(recipeId, ingredient.id(), ingredient.quantity(), unit, ingredient.notes());
        } catch (RuntimeException ignored) {}
    }

    @Override
    public void saveAll(Long recipeId, List<IngredientDTO> ingredients) {
        validator.validateRecipeExistence(recipeId);
        List<Long> recipeIngredients = new java.util.ArrayList<>(ingredientRecipeRepository.findAll(recipeId).stream()
                .map(Ingredient::getId).toList());

        for (IngredientDTO ingredient : ingredients) {
            recipeIngredients.remove(ingredient.id());

            save(recipeId, ingredient);
        }

        deleteAll(recipeId, recipeIngredients);
    }

    @Override
    public void delete(Long recipeId, Long ingredientId) {
        try {
            ingredientRecipeRepository.delete(recipeId, ingredientId);
        } catch (RuntimeException ignored) {}
    }

    @Override
    public void deleteAll(Long recipeId, List<Long> ingredients) {
        validator.validateRecipeExistence(recipeId);

        for (Long ingredientId : ingredients) {
            delete(recipeId, ingredientId);
        }
    }

    @Override
    public List<IngredientRecipe> findAll(Long recipeId) {
        validator.validateRecipeExistence(recipeId);
        return ingredientRecipeRepository.findAll(recipeId);
    }
}
