package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.DTO.RecipeInfoDTO;
import ru.itis.flavorful_book.DTO.RecipePreviewDTO;
import ru.itis.flavorful_book.DTO.RecipeSaveDTO;
import ru.itis.flavorful_book.models.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe save(RecipeSaveDTO recipeSaveDTO);

    void updateViews(Long id);

    boolean deleteById(Long id);

    RecipeSaveDTO findByIdSaveDTO(Long id);

    RecipeInfoDTO findByIdInfoDTO(Long id);

    List<RecipePreviewDTO> findAll();

    List<RecipePreviewDTO> findAll(List<Long> ingredients, boolean isStrictMatchIngredients,
                                   List<Long> categories, boolean isStrictMatchCategories,
                                   Integer activeStart, Integer activeEnd, Integer totalStart, Integer totalEnd);

    List<RecipePreviewDTO> findManyByUserFavorites(Long userId);

    List<RecipePreviewDTO> findManyByUserId(Long userId);

    void addToFavorites(Long userId, Long recipeId);

    void deleteFromFavorites(Long userId, Long recipeId);

    boolean isInFavorites(Long userId, Long recipeId);
}
