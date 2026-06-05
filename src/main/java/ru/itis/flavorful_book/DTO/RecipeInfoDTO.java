package ru.itis.flavorful_book.dto;

import ru.itis.flavorful_book.entity.Recipe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecipeInfoDTO(
        Long id,
        String title,
        String description,
        String instructions,
        int activeCookingTime,
        int totalCookingTime,
        int servings,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String username,
        String avatarUrl,
        Long views,
        long likes,
        BigDecimal rating
) {
    public static RecipeInfoDTO from(Recipe recipe, long likes) {
        return new RecipeInfoDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getActiveCookingTime(),
                recipe.getTotalCookingTime(),
                recipe.getServings(),
                recipe.getImageUrl(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt(),
                recipe.getAuthor().getId(),
                recipe.getAuthor().getUsername(),
                recipe.getAuthor().getAvatarUrl(),
                recipe.getViews(),
                likes,
                recipe.getRating()
        );
    }
}
