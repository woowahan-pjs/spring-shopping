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
src/ (main app - Spring Boot)          module-product/         module-member/
├── controller/                        ├── Product.java        ├── Member.java
│   ├── ProductController              ├── usecase/            ├── usecase/
│   └── MemberController               │   ├── CreateProduct   │   ├── RegisterMember
├── dto/                               │   ├── FindProduct     │   └── LoginMember
│   ├── ProductRequest/Response        │   ├── UpdateProduct   ├── service/ (impls)
│   └── MemberRequest/Response         │   └── DeleteProduct   └── port/
├── config/                            ├── service/ (impls)        ├── MemberRepository
│   ├── ProductConfiguration           └── port/                   ├── PasswordEncoder
│   └── MemberConfiguration                ├── ProductRepository   └── TokenProvider
└── adapter/                               └── ProfanityChecker
    ├── InMemoryProductRepository
    ├── InMemoryMemberRepository
    ├── JwtTokenProvider
    ├── BcryptPasswordEncoder
    └── PurgoMalumProfanityChecker
```

**Dependency flow:** `src/` → `module-product`, `module-member`. Modules never depend on each other.

## Code Rules

- **No database yet** — only in-memory repositories using `ConcurrentHashMap`. No JPA, JDBC, or MyBatis.
- **Domain modules are pure Java** — no Spring annotations (`@Service`, `@Repository`, `@Component`, etc.) and no Spring dependencies in their `build.gradle.kts`. They contain only POJOs, usecase interfaces, service implementations, and port interfaces.
- **Main app module (`src/`)** contains controllers, DTOs, Spring `@Configuration` classes (that wire up module services as `@Bean`), and infrastructure adapters (concrete implementations of port interfaces with Spring annotations).
- **Each usecase interface has 1–2 methods** (single responsibility). Create separate interfaces rather than combining operations.
- **Formatting:** Spotless with Google Java Style (Eclipse formatter via `google-style.xml`). Pre-commit hook runs `spotlessApply` automatically.

## Tech Stack

- Java 21, Spring Boot 3.5.9, Gradle (Kotlin DSL)
- JWT (JJWT 0.12.6) for authentication tokens
- Spring Security Crypto for BCrypt password encoding
- Cucumber 7.20.1 for BDD tests
