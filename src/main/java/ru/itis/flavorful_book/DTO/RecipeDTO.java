package ru.itis.flavorful_book.dto;

import ru.itis.flavorful_book.entity.Recipe;

import java.util.List;

public record RecipeDTO(
        Long id,
        Long authorId,
        String title,
        String description,
        String instructions,
        Integer activeCookingTime,
        Integer totalCookingTime,
        Integer servings,
        String imageUrl,
        List<Long> categories,
        List<IngredientDTO> ingredients
) {
    public static RecipeDTO from(Recipe recipe, List<Long> categoryIds, List<IngredientDTO> ingredients) {
        return new RecipeDTO(
                recipe.getId(),
                recipe.getAuthor().getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getActiveCookingTime(),
                recipe.getTotalCookingTime(),
                recipe.getServings(),
                recipe.getImageUrl(),
                categoryIds,
                ingredients
        );
    }
}
