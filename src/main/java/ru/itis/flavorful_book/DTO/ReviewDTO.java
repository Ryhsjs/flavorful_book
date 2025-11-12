package ru.itis.flavorful_book.DTO;

import java.time.LocalDateTime;

public record ReviewDTO(
        Long id,
        Long userId,
        Long recipeId,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String avatarUrl
) {
}
