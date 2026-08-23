---
name: add-javadoc
description: Adds Javadoc to production classes under src/main/java (aggregates, entities, value objects, domain exceptions, repository interfaces, and — once they exist — application/infrastructure/web classes) — documents the public contract and invariants, not restating code. Use when the user asks to add or update Javadoc for the project's source code.
---

# Add Javadoc

Use when the user wants Javadoc added to code under `src/main/java/com/recipes/**` (or points at specific
files/classes). Per `CLAUDE.md` only `domain/` exists today, but this skill isn't domain-only — apply the
same approach once `application`/`infrastructure`/`web` are built.

## 1. Scope the target files

If the user named specific files/classes, use those. If they said "the source" or "the src package"
generically, list everything under `src/main/java/com/recipes/**/*.java` and confirm the list with the
user before writing anything — the codebase grows fast and silently doc'ing the wrong set wastes a review
cycle.

## 2. Read before writing

For each target class, read the whole file, not just the signature — invariants enforced in a compact
constructor or a business method (e.g. `Recipe.publish()`'s preconditions) are the actual content of the
Javadoc; the method name alone doesn't capture them. If the class already has a `CLAUDE.md` or in-code
rationale (e.g. the `UserId`/`Email` natural-key comment), reuse that wording rather than re-deriving it.

## 3. What to document

Javadoc here documents the **public contract for a reader who doesn't want to read the implementation** —
that's a different bar than CLAUDE.md's "no comments unless the why is non-obvious" rule for inline
comments, but the same spirit of minimalism applies: say what a consumer needs, nothing more.

- **Class/record-level only**: what the type represents in the domain, and any invariant it always
  upholds (e.g. "always valid", "identity is the natural key X, immutable after creation").
- **No method-level Javadoc**, ever — not on business methods, constructors, factory methods, or
  accessors. A well-named method should be clear on its own; if it isn't, the fix is a better name or an
  inline comment on the non-obvious line, not a Javadoc block. This applies even to methods that throw —
  don't add `@throws` blocks.

Keep the class-level block short — 1-3 sentences. Don't pad with restatements of the class name.

## 4. Apply and verify

Edit the files, then run `./mvnw -q compile` to confirm nothing broke (Javadoc can still introduce a
syntax error). Run the relevant test class(es) too if you touched anything beyond comments.

## 5. Show the diff

Summarize what was documented (which classes/methods) rather than pasting every Javadoc block back into
chat — the user can review the actual diff.

## Notes

- Never invent behavior in a Javadoc block — if a method's actual precondition is unclear from the code,
  ask the user rather than guessing at business rules.
- Don't add Javadoc to test classes.
