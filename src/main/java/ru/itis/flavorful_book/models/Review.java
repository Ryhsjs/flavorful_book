package ru.itis.flavorful_book.models;

import java.time.LocalDateTime;

public class Review {

    private Long id;

    private final Long userId;

    private final Long recipeId;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Review(Long id, Long userId, Long recipeId, Integer rating, String comment, LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.recipeId = recipeId;
        setRating(rating);
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating == null || rating > 5 || rating < 1)
            this.rating = 5;
        else
            this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
