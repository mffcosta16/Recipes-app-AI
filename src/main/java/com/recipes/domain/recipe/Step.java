package com.recipes.domain.recipe;

public record Step(int order, String description) {

    private static final int MAX_DESCRIPTION_LENGTH = 500;

    public Step {
        if (order < 1) {
            throw new IllegalArgumentException("Step order must be >= 1");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Step description must not be null or blank");
        }
        description = description.trim();
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Step description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters");
        }
    }

    Step withOrder(int newOrder) {
        return new Step(newOrder, description);
    }
}