package com.recipes.domain.ingredient;

import java.util.Objects;
import java.util.UUID;

public record IngredientId(UUID value) {

    public IngredientId {
        Objects.requireNonNull(value, "IngredientId value must not be null");
    }

    public static IngredientId generate() {
        return new IngredientId(UUID.randomUUID());
    }

    public static IngredientId of(UUID value) {
        return new IngredientId(value);
    }

    public static IngredientId of(String value) {
        return new IngredientId(UUID.fromString(value));
    }
}