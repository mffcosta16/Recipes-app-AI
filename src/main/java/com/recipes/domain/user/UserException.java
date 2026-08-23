package com.recipes.domain.user;

/**
 * Thrown when a rule of the {@code User} aggregate is violated (e.g. an invalid {@link Email},
 * {@link UserName}, or {@link UserId}). Callers can catch this type to distinguish a domain
 * validation failure from an unrelated programming error, instead of catching generic JDK
 * exceptions like {@code IllegalArgumentException} or {@code NullPointerException}.
 */
public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
