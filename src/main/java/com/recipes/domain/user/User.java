package com.recipes.domain.user;

import java.util.Objects;

public class User {

    private final UserId id;
    private Username username;
    private Email email;

    private User(UserId id, Username username, Email email) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
    }

    public static User register(Username username, Email email) {
        return new User(UserId.generate(), username, email);
    }

    public static User reconstruct(UserId id, Username username, Email email) {
        return new User(id, username, email);
    }

    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}