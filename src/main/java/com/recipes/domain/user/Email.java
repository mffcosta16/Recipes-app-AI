package com.recipes.domain.user;

import java.util.regex.Pattern;

public record Email(String email) {

    private static final Pattern FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Email {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        email = email.trim();
        if (!FORMAT.matcher(email).matches()) {
            throw new IllegalArgumentException("Email is not a valid format: " + email);
        }
    }
}