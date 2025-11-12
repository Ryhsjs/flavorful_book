package ru.itis.flavorful_book.DTO;

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
        LocalDateTime updateAt,
        Long userId,
        String username,
        String avatarUrl,
        int views,
        int likes,
        Float rating
) {
}
