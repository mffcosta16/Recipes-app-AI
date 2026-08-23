package com.recipes.domain.user;

import com.recipes.domain.shared.ValueObject;

/**
 * A user's display name. Always valid and normalized (trimmed, lower-cased) once constructed.
 */
public record UserName(String name) implements ValueObject {

    private static final int MAX_LENGTH = 20;

    public UserName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }
        String trimmed = name.trim();

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username must not exceed " + MAX_LENGTH + " characters");
        }

        name = trimmed.toLowerCase();
    }
}
