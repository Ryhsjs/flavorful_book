package ru.itis.flavorful_book.DTO;

public record IngredientDTO(
        Long id,
        Integer quantity,
        String unit,
        String notes
) {
}
