# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A recipe management app (create, update, publish, delete recipes) built as a deliberate learning
exercise in OOP/OOD, DDD, and onion/clean architecture, with hand-written SQL/Postgres and a Spring Boot
backend. A React frontend will consume the API once the backend is in place. See `README.md` for the
full rationale and domain model description, and `docs/diagrams/domain-model.puml` for the class diagram.

## Commands

```bash
cp .env.example .env        # first time only, sets local Postgres credentials
./mvnw spring-boot:run       # runs the app; Spring Boot's docker-compose integration auto-starts
                              # the postgres service from docker-compose.yml and auto-wires the
                              # datasource — no manual DB setup or credentials in application config
./mvnw test                  # run all tests
./mvnw test -Dtest=RecipeTest                    # run a single test class
./mvnw test -Dtest=RecipeTest#publishSucceeds... # run a single test method
./mvnw -q compile            # fast compile check without running tests
```

The app listens on **port 8081**, not Spring Boot's default 8080 (kept free for other local services on
this machine — see `application.properties`).

## Architecture

Onion / Clean Architecture — dependencies point inward only:

```
domain          zero framework dependencies (no Spring, no jakarta.*, no Lombok). Aggregates, value
                objects, domain exceptions, repository interfaces (ports).
application     depends only on domain. Use cases that orchestrate the domain and call repository
                ports. Cross-aggregate existence checks (e.g. "does this IngredientId exist?") belong
                here, not in the domain, since they require repository access.
infrastructure  depends on domain + application + frameworks. JPA entities are separate classes from
                domain entities (never annotate a domain class with @Entity); adapters implement the
                domain's repository interfaces; mappers convert between JPA and domain objects.
web             depends only on application. Controllers call use cases; never repositories or domain
                internals directly.
```

Package layout mirrors this, then subdivides by aggregate (`domain.recipe`, `domain.ingredient`,
`domain.user`, and so on per layer).

Only `domain/` exists so far (`application`, `infrastructure`, `web` are not yet built).

## Domain model conventions

Principles established while designing `Recipe`, `Ingredient`, and `User` — apply the same thinking to
new domain code, rather than treating these as a fixed checklist:

- **Value objects are immutable and always valid.** Prefer `record`s; validate invariants where the
  object is constructed so an invalid instance can never exist.
- **Entities protect their own invariants.** Mutation happens through business methods that express
  intent (`publish()`, not `setStatus(PUBLISHED)`); identity (not field values) defines equality.
- **Object creation is explicit about intent.** Static factory methods (e.g. distinguishing "create new"
  from "rehydrate from storage") are preferred over public constructors; reach for a dedicated Factory
  class only when creation genuinely needs to coordinate multiple aggregates or external state.
- **Aggregates reference each other by ID only**, never by holding another aggregate's object directly —
  that's what keeps each aggregate an independent consistency boundary. Checking whether a referenced ID
  actually exists is an application-layer concern (it needs repository access), not the domain's.
- **Domain exceptions live close to the rule they enforce** and are kept lightweight — this project
  nests one exception type per aggregate rather than a class per violation.
- **Repository interfaces are part of the domain** (they express what persistence the domain needs);
  only their implementations belong in infrastructure.

For the exact rules `Recipe` currently enforces (publish invariants, lifecycle, duplicate ingredients,
step ordering), read `Recipe.java` — it's the source of truth, not this file.

## Git workflow

- **Never commit directly to `main`.** All changes go through a feature branch and a PR, even for small
  or solo changes — this keeps `main` always green and lets CI validate a change before it lands.
- **Commit messages are a single line** (summary only, plus the `Co-Authored-By` trailer). Don't add a
  body paragraph unless the *why* genuinely isn't obvious from the diff or summary alone (e.g. a
  non-obvious bug fix or a deliberate architectural tradeoff) — don't add one reflexively.

## Tooling

- CI (`.github/workflows/ci.yml`) runs GitLeaks secret scanning (posts findings as a PR comment) and
  `./mvnw test` on every push/PR to `main`. JaCoCo coverage is intentionally not wired in yet — add it
  once the domain test suite is back in place, since an empty suite would fail any coverage gate.