package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Recipe;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> findWithFilters(
            List<Long> ingredientIds, boolean strictIngredients,
            List<Long> categoryIds, boolean strictCategories,
            int activeStart, int activeEnd,
            int totalStart, int totalEnd
    );
}
