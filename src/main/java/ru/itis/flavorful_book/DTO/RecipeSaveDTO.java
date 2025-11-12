package ru.itis.flavorful_book.DTO;

import java.util.List;

public record RecipeSaveDTO(
        Long id,
        String title,
        String description,
        String instructions,
        Integer activeCookingTime,
        Integer totalCookingTime,
        Integer servings,
        String imageUrl,
        Long userId,
        List<Long> categories,
        List<IngredientDTO> ingredients
) {
}
