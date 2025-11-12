package ru.itis.flavorful_book.models.enums;

public enum Unit {
    GRAMS("г"),
    KILOGRAMS("кг"),
    MILLILITERS("мл"),
    LITERS("л"),
    TEASPOON("ч. л."),
    TABLESPOON("ст. л."),
    CUP("стакан"),
    PIECE("шт."),
    PINCH("щепотка"),
    CLOVE("зубчик"),
    BUNCH("пучок"),
    SLICE("ломтик"),
    LEAF("лист");

    private final String unit;

    private Unit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
