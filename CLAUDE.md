# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./gradlew build              # Build all modules + run tests + spotless check
./gradlew test               # Run all tests (Cucumber acceptance tests)
./gradlew :module-product:build   # Build only module-product
./gradlew :module-member:build    # Build only module-member
./gradlew spotlessApply      # Auto-format all Java files (runs on pre-commit hook)
./gradlew spotlessCheck      # Check formatting without fixing
./gradlew asciidoctor        # Generate REST API docs (runs tests first)
```

Single test class: `./gradlew test --tests "shopping.CucumberTest"`

## Architecture: Multi-Module

```
src/ (main app - Spring Boot)          module-product/             module-member/
├── controller/                        ├── Product.java (@Entity)  ├── Member.java (@Document)
│   ├── ProductController              ├── ProductName.java        ├── Wish.java
│   └── MemberController               ├── usecase/                ├── usecase/
├── dto/                               │   ├── CreateProduct       │   ├── RegisterMember
│   ├── ProductRequest/Response        │   ├── FindProduct         │   └── LoginMember
│   └── MemberRequest/Response         │   ├── UpdateProduct       ├── service/ (impls)
├── config/                            │   └── DeleteProduct       └── port/
│   ├── ProductConfiguration           ├── service/ (impls)            ├── MemberRepository
│   ├── MemberConfiguration            └── port/                       ├── PasswordEncoder
│   └── WishConfiguration                  ├── ProductRepository       └── TokenProvider
├── SpringDataProductRepository            └── ProfanityChecker
├── SpringDataMemberRepository
├── JwtTokenProvider
├── BcryptPasswordEncoder
└── PurgoMalumProfanityChecker
```

**Dependency flow:** `src/` → `module-product`, `module-member`. Modules never depend on each other.

## Database Architecture

- **Product → MySQL** (JPA `@Entity` with Flyway migrations)
- **Member + Wish → MongoDB** (Spring Data `@Document`, wishes embedded as array)
- Domain classes (Product, Member) ARE the entities/documents directly — no separate entity/document classes, no toDomain/fromDomain.
- `SpringDataProductRepository extends JpaRepository<Product, UUID>, ProductRepository`
- `SpringDataMemberRepository extends MongoRepository<Member, UUID>, MemberRepository`
- Docker Compose provides MySQL 8.0 and MongoDB 7.0 for local dev.
- Tests use H2 (for JPA) and embedded MongoDB (flapdoodle).

## Code Rules

- **Domain modules have Spring dependencies** — `module-product` uses `jakarta.persistence-api` for JPA annotations, `module-member` uses `spring-data-mongodb` for MongoDB annotations.
- **Domain classes are used directly as entities/documents** — no separate adapter entity classes.
- **Main app module (`src/`)** contains controllers, DTOs, Spring `@Configuration` classes (that wire up module services as `@Bean`), and Spring Data repository interfaces.
- **Each usecase interface has 1–2 methods** (single responsibility). Create separate interfaces rather than combining operations.
- **Formatting:** Spotless with Google Java Style (Eclipse formatter via `google-style.xml`). Pre-commit hook runs `spotlessApply` automatically.

## Tech Stack

- Java 21, Spring Boot 3.5.9, Gradle (Kotlin DSL)
- MySQL 8.0 (Product), MongoDB 7.0 (Member + Wish)
- Flyway for MySQL schema migrations
- JWT (JJWT 0.12.6) for authentication tokens
- Spring Security Crypto for BCrypt password encoding
- Cucumber 7.20.1 for BDD tests
