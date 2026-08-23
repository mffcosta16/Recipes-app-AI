package com.recipes.domain.shared;

/**
 * Marker for value objects that serve as an aggregate's identity. Aggregates reference each
 * other only by their {@code DomainId}, never by holding another aggregate directly.
 */
public interface DomainId extends ValueObject {

}
