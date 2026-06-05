package ru.itis.flavorful_book.dto;

import ru.itis.flavorful_book.entity.Review;

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
    public static ReviewDTO from(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getId(),
                review.getRecipe().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getUser().getUsername(),
                review.getUser().getAvatarUrl()
        );
    }
}
