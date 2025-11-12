package ru.itis.flavorful_book.DTO;

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
        Float rating,
        String ingredients
) {
}
