package ru.itis.flavorful_book.models;

import java.time.LocalDateTime;

public class Recipe {

    private Long id;

    private String title;

    private String description;

    private String instructions;

    private Integer activeCookingTime;

    private Integer totalCookingTime;

    private Integer servings;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private final Long userId;

    private Integer views;

    private Float rating;

    public Recipe(Long id,
                  String title,
                  String description,
                  String instructions,
                  Integer activeCookingTime,
                  Integer totalCookingTime,
                  Integer servings,
                  String imageUrl,
                  LocalDateTime createdAt,
                  LocalDateTime updateAt,
                  Long userId,
                  Integer views,
                  Float rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.activeCookingTime = activeCookingTime;
        this.totalCookingTime = totalCookingTime;
        this.servings = servings;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.userId = userId;
        this.views = views;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getActiveCookingTime() {
        return activeCookingTime;
    }

    public void setActiveCookingTime(Integer activeCookingTime) {
        this.activeCookingTime = activeCookingTime;
    }

    public Integer getTotalCookingTime() {
        return totalCookingTime;
    }

    public void setTotalCookingTime(Integer totalCookingTime) {
        this.totalCookingTime = totalCookingTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
