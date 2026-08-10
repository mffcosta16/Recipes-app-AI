package com.recipes.domain.recipe;

import java.util.Objects;
import java.util.UUID;

public record RecipeId(UUID value) {

    public RecipeId {
        Objects.requireNonNull(value, "RecipeId value must not be null");
    }

    public static RecipeId generate() {
        return new RecipeId(UUID.randomUUID());
    }

    public static RecipeId of(UUID value) {
        return new RecipeId(value);
    }

    public static RecipeId of(String value) {
        return new RecipeId(UUID.fromString(value));
    }
}