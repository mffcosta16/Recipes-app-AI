package com.recipes.domain.ingredient;

import java.util.Objects;

public class Ingredient {

    private final IngredientId id;
    private IngredientName name;

    private Ingredient(IngredientId id, IngredientName name) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public static Ingredient register(IngredientName name) {
        return new Ingredient(IngredientId.generate(), name);
    }

    public static Ingredient reconstruct(IngredientId id, IngredientName name) {
        return new Ingredient(id, name);
    }

    public IngredientId getId() {
        return id;
    }

    public IngredientName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient ingredient)) return false;
        return id.equals(ingredient.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}