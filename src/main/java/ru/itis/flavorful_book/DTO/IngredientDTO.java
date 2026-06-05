package ru.itis.flavorful_book.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.enums.Unit;

import java.math.BigDecimal;

public record IngredientDTO(
        @NotNull Long id,
        String name,
        @NotNull @DecimalMin("0.01") BigDecimal quantity,
        @NotNull Unit unit,
        @Size(max = 255) String notes
) {
    public static IngredientDTO from(IngredientRecipe ir) {
        return new IngredientDTO(
                ir.getIngredient().getId(),
                ir.getIngredient().getName(),
                ir.getQuantity(),
                ir.getUnit(),
                ir.getNotes()
        );
    }
}
