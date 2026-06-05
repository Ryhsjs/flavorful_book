package ru.itis.flavorful_book.dto;

import ru.itis.flavorful_book.entity.Recipe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecipePreviewDTO(
        Long id,
        String title,
        String description,
        int activeCookingTime,
        int totalCookingTime,
        String imageUrl,
        LocalDateTime createdAt,
        Long userId,
        String username,
        BigDecimal rating,
        String ingredients
) {
    public static RecipePreviewDTO from(Recipe recipe, String ingredientNames) {
        return new RecipePreviewDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getActiveCookingTime(),
                recipe.getTotalCookingTime(),
                recipe.getImageUrl(),
                recipe.getCreatedAt(),
                recipe.getAuthor().getId(),
                recipe.getAuthor().getUsername(),
                recipe.getRating(),
                ingredientNames
        );
    }
}
