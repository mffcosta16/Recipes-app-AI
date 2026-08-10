package com.recipes.domain.recipe;

import com.recipes.domain.ingredient.IngredientId;
import com.recipes.domain.user.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private final RecipeId id;
    private Title title;
    private final UserId authorId;
    private RecipeStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private final List<RecipeIngredient> ingredients;
    private final List<Step> steps;

    private Recipe(RecipeId id, Title title, UserId authorId, RecipeStatus status,
                    Instant createdAt, Instant updatedAt,
                    List<RecipeIngredient> ingredients, List<Step> steps) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.authorId = Objects.requireNonNull(authorId, "authorId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        this.ingredients = new ArrayList<>(ingredients);
        this.steps = new ArrayList<>(steps);
    }

    public static Recipe draft(Title title, UserId authorId) {
        Instant now = Instant.now();
        return new Recipe(RecipeId.generate(), title, authorId, RecipeStatus.DRAFT, now, now, List.of(), List.of());
    }

    public static Recipe reconstruct(RecipeId id, Title title, UserId authorId, RecipeStatus status,
                                      Instant createdAt, Instant updatedAt,
                                      List<RecipeIngredient> ingredients, List<Step> steps) {
        return new Recipe(id, title, authorId, status, createdAt, updatedAt, ingredients, steps);
    }

    public void addIngredient(IngredientId ingredientId, Quantity quantity) {
        boolean alreadyPresent = ingredients.stream()
                .anyMatch(recipeIngredient -> recipeIngredient.ingredientId().equals(ingredientId));
        if (alreadyPresent) {
            throw RecipeDomainException.duplicateIngredient(ingredientId);
        }
        ingredients.add(new RecipeIngredient(ingredientId, quantity));
        touch();
    }

    public void removeIngredient(IngredientId ingredientId) {
        if (status == RecipeStatus.PUBLISHED && ingredients.size() <= 1) {
            throw RecipeDomainException.publishedInvariantViolation(id);
        }
        ingredients.removeIf(recipeIngredient -> recipeIngredient.ingredientId().equals(ingredientId));
        touch();
    }

    public void addStep(String description) {
        steps.add(new Step(steps.size() + 1, description));
        touch();
    }

    public void removeStep(int order) {
        if (status == RecipeStatus.PUBLISHED && steps.size() <= 1) {
            throw RecipeDomainException.publishedInvariantViolation(id);
        }
        steps.removeIf(step -> step.order() == order);
        reindexSteps();
        touch();
    }

    public void moveStep(int fromOrder, int toOrder) {
        int fromIndex = fromOrder - 1;
        int toIndex = toOrder - 1;
        if (fromIndex < 0 || fromIndex >= steps.size() || toIndex < 0 || toIndex >= steps.size()) {
            throw new IllegalArgumentException("Step order out of range");
        }
        Step step = steps.remove(fromIndex);
        steps.add(toIndex, step);
        reindexSteps();
        touch();
    }

    public void rename(Title newTitle) {
        this.title = Objects.requireNonNull(newTitle, "newTitle must not be null");
        touch();
    }

    public void publish() {
        if (status != RecipeStatus.DRAFT) {
            throw RecipeDomainException.invalidStateTransition(status, RecipeStatus.PUBLISHED);
        }
        if (ingredients.isEmpty() || steps.isEmpty()) {
            throw RecipeDomainException.incompleteForPublish(id);
        }
        status = RecipeStatus.PUBLISHED;
        touch();
    }

    public void archive() {
        if (status != RecipeStatus.PUBLISHED) {
            throw RecipeDomainException.invalidStateTransition(status, RecipeStatus.ARCHIVED);
        }
        status = RecipeStatus.ARCHIVED;
        touch();
    }

    private void reindexSteps() {
        for (int i = 0; i < steps.size(); i++) {
            steps.set(i, steps.get(i).withOrder(i + 1));
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public RecipeId getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public UserId getAuthorId() {
        return authorId;
    }

    public RecipeStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<RecipeIngredient> getIngredients() {
        return List.copyOf(ingredients);
    }

    public List<Step> getSteps() {
        return List.copyOf(steps);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe recipe)) return false;
        return id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static class RecipeDomainException extends RuntimeException {

        private RecipeDomainException(String message) {
            super(message);
        }

        public static RecipeDomainException duplicateIngredient(IngredientId ingredientId) {
            return new RecipeDomainException("Ingredient " + ingredientId.value() + " is already part of this recipe");
        }

        public static RecipeDomainException incompleteForPublish(RecipeId recipeId) {
            return new RecipeDomainException("Recipe " + recipeId.value()
                    + " must have at least one ingredient and one step to be published");
        }

        public static RecipeDomainException publishedInvariantViolation(RecipeId recipeId) {
            return new RecipeDomainException("Recipe " + recipeId.value()
                    + " is published and must keep at least one ingredient and one step");
        }

        public static RecipeDomainException invalidStateTransition(RecipeStatus from, RecipeStatus to) {
            return new RecipeDomainException("Cannot transition recipe from " + from + " to " + to);
        }
    }
}