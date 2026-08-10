# Recipes App

A recipe management application (create, update, publish and delete recipes) built as a hands-on learning
project. The goal isn't just a working app, it's a deliberate exercise in:

- **OOP (Object-Oriented Principles) / OOD (Object-Oriented Design)** — small, well-encapsulated objects with behavior, not anemic data holders;
- **DDD (Domain-Driven Design)** — aggregates, value objects, invariants enforced by the domain itself;
- **Onion / Clean Architecture** — dependencies point inward; the domain knows nothing about Spring,
  JPA, or HTTP;
- **PostgreSQL / SQL** — hand-written schema and queries rather than framework-generated ones;
- **React** — a frontend consuming the backend API, once the backend is solid.

## Tech stack

| Concern             | Choice                                                            |
|---------------------|--------------------------------------------------------------------|
| Language / runtime  | Java 21                                                            |
| Framework           | Spring Boot 4.1 (Web, Data JPA, Validation)                        |
| Build tool          | Maven                                                              |
| Database            | PostgreSQL 16, run via Docker Compose                              |
| Local dev wiring    | `spring-boot-docker-compose` (auto-starts Postgres, auto-configures the datasource — no credentials in application config) |
| Frontend (later)    | React                                                              |

## Domain model

Three aggregates, each responsible for its own invariants:

- **`User`** — owns recipes (`Recipe.authorId` references it by ID only, never by object)
- **`Ingredient`** — a reusable catalog entry, referenced by ID from recipes
- **`Recipe`** — the core aggregate. Enforces:
  - non-blank title
  - positive ingredient quantities
  - no duplicate ingredients
  - automatically re-indexed step ordering (clients never set step order directly)
  - a one-way lifecycle: `DRAFT → PUBLISHED → ARCHIVED`
    - `publish()` requires at least one ingredient and one step
    - while `PUBLISHED`, edits that would violate that invariant are rejected rather than silently
      reverting the recipe to `DRAFT`
    - `archive()` is only valid from `PUBLISHED`

A PlantUML diagram of the model lives at [`docs/diagrams/domain-model.puml`](docs/diagrams/domain-model.puml).

Cross-aggregate references are always by ID (e.g. `Recipe` holds a `UserId`, not a `User`) — this keeps
each aggregate an independent consistency boundary, per standard DDD practice.

## Architecture

Onion / Clean Architecture, dependencies pointing inward only:

```
domain          <- entities, value objects, domain exceptions, repository interfaces (ports).
                   No framework dependencies at all.
application     <- use cases, orchestrates the domain, depends only on domain.
infrastructure  <- JPA entities, Spring Data repositories, repository port implementations,
                   config. Depends on domain + application.
web             <- REST controllers, request/response DTOs. Depends on application.
```

Package layout mirrors this, then subdivides by aggregate:

```
com.recipes
├── domain
│   ├── recipe
│   ├── ingredient
│   └── user
├── application
├── infrastructure
│   └── persistence
└── web
```

## Running locally

Prerequisites: Java 21, Docker.

```bash
cp .env.example .env   # set a local Postgres password
./mvnw spring-boot:run
```

Spring Boot's Docker Compose integration starts the `postgres` service defined in
[`docker-compose.yml`](docker-compose.yml) automatically and wires the datasource for you.

The app listens on **port 8081** (not the Spring Boot default 8080) to avoid clashing with other local
services.

Run the tests:

```bash
./mvnw test
```

## Status

Backend bootstrap and the domain layer's design are in place. Application/infrastructure/web layers,
persistence (Postgres schema + migrations), CI, and the React frontend are still to come.