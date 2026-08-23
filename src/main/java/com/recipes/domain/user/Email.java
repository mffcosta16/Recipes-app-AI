package com.recipes.domain.user;

import com.recipes.domain.shared.ValueObject;

import java.util.regex.Pattern;

/**
 * A user's email address. Always valid and normalized (trimmed, lower-cased) once constructed;
 * serves as the immutable natural key for {@link UserId} — see {@link UserId}.
 */
public record Email(String email) implements ValueObject {

    private static final Pattern FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Email {
        if ( email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        String trimmed = email.trim();

        if(!FORMAT.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Email is not a valid format: " + email);
        }

        email = trimmed.toLowerCase();
    }
}
