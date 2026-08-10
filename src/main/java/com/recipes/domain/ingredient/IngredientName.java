package com.recipes.domain.ingredient;

public record IngredientName(String name) {

    private static final int MAX_LENGTH = 50;

    public IngredientName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("IngredientName must not be null or blank");
        }
        name = name.trim();
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("IngredientName must not exceed " + MAX_LENGTH + " characters");
        }
    }
}