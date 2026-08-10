package com.recipes.domain.recipe;

public record Title(String value) {

    private static final int MAX_LENGTH = 150;

    public Title {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Title must not exceed " + MAX_LENGTH + " characters");
        }
    }
}