package ru.itis.flavorful_book.models;

import ru.itis.flavorful_book.models.enums.Unit;

public class IngredientRecipe extends Ingredient{
    private final Long recipeId;

    private Integer quantity;

    private Unit unit;

    private String notes;

    public IngredientRecipe(Long id, String name, Long recipeId, Integer quantity, Unit unit, String notes) {
        super(id, name);
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unit = unit;
        this.notes = notes;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
