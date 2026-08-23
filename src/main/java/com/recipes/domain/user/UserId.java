package com.recipes.domain.user;

import com.recipes.domain.shared.DomainId;

/**
 * Identity of a {@code User} aggregate. Email is the natural key for user identity: it must
 * never change after account creation. A user who wants a different email must create a new
 * account, not mutate this one.
 */
public record UserId(Email email) implements DomainId {

    public UserId {
        if (email == null) {
            throw new UserException("Email is required");
        }
    }
}
