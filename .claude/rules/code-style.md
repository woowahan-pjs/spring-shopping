---
description: Java 코드를 작성하거나 수정할 때 적용
globs: "**/*.java"
---

# Code Style

- Use 4-space indentation for all Java files (no tabs)
- Before committing, run `./gradlew spotlessApply` to auto-format

## Code Rules
- No database yet — only plain POJO classes and interfaces
- No JPA, JDBC, or MyBatis at this stage
- The main app module (src/) contains only controllers, DTOs, Spring configurations, and infrastructure adapters
- All service classes and repository/port interfaces must live in their respective domain modules

## Architecture
- Each usecase interface should have one or two methods only (single responsibility)
- module-product and module-member must not depend on each other
- Modules are pure Java libraries with no Spring or framework dependencies

## Value Objects & Factory Pattern
- 도메인에서 검증이 필요한 원시 타입은 value object로 감싸고, 전용 factory 클래스를 통해서만 생성한다
- value object 생성자는 package-private으로 선언하고, factory만 호출할 수 있도록 ArchUnit 테스트로 강제한다
- 도메인 엔티티는 원시 타입(`String`) 대신 value object(`ProductName`)를 필드로 갖는다
- 검증 로직(null, 길이, 포맷, 비속어 등)은 서비스가 아닌 factory에 집중시켜 중복을 제거한다
- usecase 인터페이스는 도메인 엔티티 대신 raw 파라미터를 받고, 서비스 내부에서 factory를 통해 value object를 생성한다
- ArchUnit 테스트는 전체 프로젝트를 스캔할 수 있도록 root 모듈(`src/test/`)에 둔다
- 예: `ProductName`(value object) + `ProductNameFactory`(factory) → `ProductNameArchTest`(생성자 호출 제약 검증)