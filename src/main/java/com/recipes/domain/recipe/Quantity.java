package com.recipes.domain.recipe;

import java.math.BigDecimal;
import java.util.Objects;

public record Quantity(BigDecimal amount, Unit unit) {

    public Quantity {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(unit, "unit must not be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity amount must be positive");
        }
    }
}