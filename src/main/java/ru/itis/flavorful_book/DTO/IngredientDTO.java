package ru.itis.flavorful_book.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.itis.flavorful_book.entity.IngredientRecipe;

import java.math.BigDecimal;

public record IngredientDTO(
        @NotNull Long id,
        @NotNull @DecimalMin("0.01") BigDecimal quantity,
        @NotNull @Size(max = 50) String unit,
        @Size(max = 255) String notes
) {
    public static IngredientDTO from(IngredientRecipe ir) {
        return new IngredientDTO(
                ir.getIngredient().getId(),
                ir.getQuantity(),
                ir.getUnit().toString(),
                ir.getNotes()
        );
    }
}
