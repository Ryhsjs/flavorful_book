package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.dto.IngredientDTO;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.Recipe;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IngredientRecipeService {
    void save(Recipe recipe, IngredientDTO ingredient);

    void saveAll(Recipe recipe, List<IngredientDTO> ingredients);

    void delete(Long recipeId, Long ingredientId);

    void deleteAll(Long recipeId, List<Long> ingredients);

    List<IngredientRecipe> findAll(Long recipeId);

    List<IngredientDTO> findAllDTOByRecipeId(Long recipeId);

    Map<Long, String> getIngredientNamesByRecipeIds(Collection<Long> recipeIds);
}
