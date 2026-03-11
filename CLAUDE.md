# Spring Shopping – AI Assistant Guidelines

## Domain Context
본 프로젝트는 온라인 쇼핑몰을 위한 간단한 HTTP API(회원, 상품, 위시 리스트)를 제공하는 백엔드 서버입니다.
도메인 모델링 및 용어 사전은 아래 문서를 참고하세요.

- 도메인 문서: [용어 사전 및 모델링](./docs/shopping-domain-modeling.md)

## Tech Stack & Language
- Primary Language: Kotlin (반드시 Java가 아닌 Kotlin으로 코드를 작성하세요.)
- Framework: Spring Boot
- **Note**: 과제 요구사항의 'Java Style Guide', 'switch문 금지', '3항 연산자 금지' 규칙은 Kotlin 환경에 맞게 해석하여 적용하세요. (예: `when`문 사용 지양, `if-else` 표현식을 통한 Early Return 지향 등)

## Common Commands

```bash
# Backend (Spring Boot & Gradle)
./gradlew clean build           # 전체 빌드 (테스트 포함)
./gradlew clean build -x test   # 테스트 제외 빌드
./gradlew test                  # 단위 및 통합 테스트 실행
./gradlew bootRun               # 스프링 부트 서버 실행
```

## Coding Conventions

> 주의: 아래 제약사항은 과제 통과를 위한 핵심 조건이므로 어떠한 예외도 허용하지 않습니다.

- 들여쓰기 및 포맷팅
  - [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 기본으로 하되, 들여쓰기(Indentation)는 4 spaces를 사용합니다. (2 spaces 절대 금지)
  - 들여쓰기 단계(Depth)는 최대 2단계까지만 허용합니다. 3단계가 될 경우 반드시 메서드를 분리하세요.

- 메서드 제약
  - 함수(메서드)의 길이는 15줄을 초과할 수 없습니다.
  - 한 함수는 단 한 가지의 일만 하도록 작게 분리하세요.

- 키워드 및 연산자 사용 금지
  - else 키워드 사용 금지: if문에서 값을 반환(Early Return)하는 방식으로 구현하세요.
  - switch 문 사용 금지.
  - 3항 연산자(? :) 사용 금지.

## Architecture & Implementation Rules

### 1. 패키지 구조
단일 모듈 환경이지만 추후 멀티 모듈 분리를 고려하여 패키지를 물리적으로 엄격하게 구분합니다.

- `api`: 웹/프레젠테이션 계층. REST Controller, `Request`/`Response` DTO가 위치합니다.
- `application`: 응용 계층. 비즈니스 흐름만 제어하는 응용 서비스, 트랜잭션 관리, `Command`/`Result` DTO가 위치합니다. **주의: 이 계층에서 도메인 규칙을 직접 검증하거나 비즈니스 로직을 짜지 마세요.**
- `domain`: 핵심 비즈니스 로직. 순수 Kotlin 도메인 모델, **도메인 서비스(여러 도메인 간의 로직/정책 처리를 위한 상태 없는 서비스)**, 도메인 인터페이스(Port)가 위치합니다. (JPA 및 웹 프레임워크 의존성 절대 금지)
- `storage` (또는 `storage:db`): 데이터베이스 영속성 계층. JPA Entity와 Spring Data JPA Repository 인터페이스 및 구현체가 위치합니다.
- `infrastructure`: 외부 API 연동, 보안 구현체(PasswordEncoder 등) 등 외부 인프라 통신 및 기술 세부 구현체가 위치합니다.
- `support`: 시스템 전반에서 공통으로 참조하는 모듈. 전역 예외 처리(`ErrorType`, `CoreException`), 공통 유틸리티, `BaseEntity` 등이 위치합니다.

### 2. DTO 분리 원칙

- 레이어 간 결합도를 낮추기 위해 용도에 맞는 DTO를 엄격히 분리합니다.
- Web(Controller) 계층: `Request`, `Response` 네이밍을 사용합니다.
- Service 계층: 비즈니스 로직 입력과 결과는 `Command`, `Result` 네이밍을 사용합니다.

### 3. 검증(Validation) 및 예외 처리

- 형식적 검증: 입력값의 길이, null 여부, 특수문자 제한 등은 Controller 계층의 Validator(또는 어노테이션)에서 처리합니다.
- 비즈니스 정책 검증: 상품명의 비속어 포함 여부, 소유권 검증 등은 도메인 모델이나 Service 로직에서 수행합니다.
- 외부 API 예외 처리: `PurgoMalum` API 연동 시 네트워크 타임아웃, 5xx 에러 등에 대한 안전한 폴백(Fallback) 또는 Custom Exception 처리를 반드시 구현해야 합니다.

### 4. 도메인 모델링

- 순수 도메인 로직과 JPA Entity를 분리합니다.
- 비즈니스 로직(Service)에서는 프레임워크 기술에 종속되지 않은 순수 객체(Domain)를 다루고, 영속화가 필요할 때만 Entity로 변환(`toEntity` 등)하여 사용합니다.

### 5. API 응답 형식

- 모든 API 응답은 JSON 형식을 사용합니다.
- 실패 응답의 경우 클라이언트가 원인을 명확히 알 수 있도록 에러 메시지와 상태 코드를 일관된 포맷으로 반환하세요.
- 에러 형식과 메시지는 "src.main.kotlin.shopping.support.error" 위치의 ErrorType 및 ErrorCode를 참고하세요.

## Testing Strategy

테스트 도구: `JUnit 5`와 `AssertJ`를 반드시 사용합니다.

1. 테스트 커버리지
    - 비즈니스적으로 중요한 부분(예: 상품명 유효성 검사, 비속어 필터링, 위시 리스트 소유권 검증)을 최우선으로 검증합니다.
2. BDD 스타일 작성
    - 모든 테스트 코드는 BDD(Behavior-Driven Development) 패턴인 `Given / When / Then` 구조를 주석으로 명시하여 작성합니다.
3. 독립성과 네이밍
    - 각 테스트는 독립적으로 실행 가능해야 하며, 상태를 공유하지 않습니다.
    - 테스트 메서드 이름은 비즈니스 관점에서 행위와 결과를 명확하게 나타내야 합니다. (예: `상품_이름이_15자를_초과하면_예외가_발생한다`)