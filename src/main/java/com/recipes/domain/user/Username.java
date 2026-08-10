package com.recipes.domain.user;

public record Username(String name) {

    private static final int MAX_LENGTH = 20;

    public Username {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }
        name = name.trim();
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username must not exceed " + MAX_LENGTH + " characters");
        }
    }
}