package com.recipes.domain.recipe;

import com.recipes.domain.ingredient.IngredientId;

import java.util.Objects;

public record RecipeIngredient(IngredientId ingredientId, Quantity quantity) {

    public RecipeIngredient {
        Objects.requireNonNull(ingredientId, "ingredientId must not be null");
        Objects.requireNonNull(quantity, "quantity must not be null");
    }
}