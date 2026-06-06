package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.api.dto.ExternalMealDTO;

public interface ExternalRecipeService {
    ExternalMealDTO getMealOfTheDay();
}
