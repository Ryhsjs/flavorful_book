package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.DTO.IngredientDTO;
import ru.itis.flavorful_book.entity.IngredientRecipe;

import java.util.List;

public interface IngredientRecipeService {
    void save(Long recipeId, IngredientDTO ingredient);

    void saveAll(Long recipeId, List<IngredientDTO> ingredients);

     void delete(Long recipeId, Long ingredientId);

    void deleteAll(Long recipeId, List<Long> ingredients);

    List<IngredientRecipe> findAll(Long recipeId);
}
