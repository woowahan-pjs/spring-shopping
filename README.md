# Spring Shopping

쇼핑몰 핵심 흐름인 회원, 상품, 위시리스트를 구현한 Spring Boot 과제입니다.  
인증은 `access token + refresh token` 구조를 사용하며, 도메인 규칙은 값 객체와 도메인 메서드로 분리했습니다.

## 문서

| 링크                                           | 설명 |
|----------------------------------------------| --- |
| [docs/features.md](docs/features.md)         | 기능 구현 현황 |
| [docs/architecture.md](docs/architecture.md) | 프로젝트 구조 및 ERD |
| [docs/class-design.md](docs/class-design.md) | 객체 설계 |
 | [docs/note.md](docs/note.md)                 | 노팅 |

## 실행 환경

| 항목 | 값 |
| --- | --- |
| Java | 21 |
| 빌드 도구 | Gradle Wrapper |
| 기본 DB | H2 in-memory |
| 인증 | JWT access token + refresh token |
| Refresh Token | 7일 유효, HttpOnly 쿠키 사용 |

로컬 실행은 별도 DB 없이 가능합니다.  
기본 설정은 [application.yml](src/main/resources/application.yml)에 있습니다.

## 패키지 구조

패키지는 기능 기준으로 나누고, 각 기능 내부는 `adapter/in/api / adapter/in/web / domain / service / port/out / adapter/out` 역할로 분리했습니다.
REST 컨트롤러와 요청/응답 DTO는 `adapter/in/api`, 인터셉터·인자 해석기·쿠키 매니저·접근 정책은 `adapter/in/web`에 둡니다. 외부 저장소나 외부 HTTP 연동은 `adapter/out`에 두고, 그 추상화는 `port/out`에 둡니다. 핵심 유스케이스와 검증 조합 로직은 `service`에 둬서 서비스가 구현 대신 포트에 의존하도록 유지합니다.

| 패키지 | 책임 |
| --- | --- |
| `auth` | 인증 정책, JWT 파싱, refresh token 회전, 인터셉터 |
| `member` | 회원 가입/로그인, 회원 상태와 역할 규칙 |
| `product/service` | 상품 핵심 서비스와 비속어 검사 조합 로직 |
| `auth/adapter/in/web` | 인증 인터셉터, 접근 정책, 쿠키 관리, 현재 회원 주입 |
| `*/adapter/in/api` | 각 도메인의 REST 컨트롤러와 요청/응답 DTO |
| `product/port/out` | 외부 의존성을 향한 아웃바운드 포트 |
| `product/adapter/out` | 외부 HTTP, 스냅샷 provider 같은 아웃바운드 어댑터 |
| `wish` | 위시리스트 추가/조회/삭제, 중복 방지 |
| `common` | 공통 예외, 에러 응답, 베이스 엔티티 |

## 핵심 시나리오

- 회원은 `POST /api/members/register`, `POST /api/members/login`, `POST /api/auth/refresh`로 가입, 로그인, 로그인 유지 흐름을 검증할 수 있습니다.
- 비회원은 `GET /api/products`, `GET /api/products/{productId}`로 상품을 조회할 수 있습니다.
- 로그인한 회원은 `POST /api/wishes`, `GET /api/wishes`, `DELETE /api/wishes/{wishId}`로 위시리스트를 관리할 수 있습니다.
- 판매자 상품 CRUD는 공개 API로 직접 계정을 만들 수 없으므로 `./gradlew e2eTest` 기준으로 검증합니다.

## 권한과 예외 처리

권한 규칙은 아래와 같습니다.

- 비회원은 상품 조회만 가능합니다.
- 회원은 상품 조회와 위시리스트 CRUD가 가능합니다.
- 판매자는 상품 CRUD가 가능합니다.
- 판매자도 본인 상품만 수정하거나 삭제할 수 있습니다.

에러 응답은 아래 형식을 사용합니다.

```json
{
  "code": "PRODUCT_NAME_TOO_LONG",
  "message": "Product name must be at most 15 characters."
}
```

대표 에러 코드는 아래와 같습니다.

| 분류 | 예시 코드 |
| --- | --- |
| 인증 | `AUTH_HEADER_REQUIRED`, `AUTH_TOKEN_INVALID` |
| refresh | `REFRESH_TOKEN_REQUIRED`, `REFRESH_TOKEN_INVALID` |
| 상품 | `PRODUCT_NAME_TOO_LONG`, `PRODUCT_OWNER_FORBIDDEN`, `PRODUCT_NAME_CONTAINS_SLANG` |
| 위시 | `WISH_ALREADY_EXISTS`, `WISH_ITEM_NOT_FOUND` |

## 테스트 전략

### Gradle 실행
단순 성공/실패 여부를 확인하기 위한 방식입니다.

| 구분 | 명령어 | 검증 내용 | 산출물 |
| --- | --- | --- | --- |
| 단위 테스트 | `./gradlew test` | 도메인 규칙, 예외 처리, 권한 분기, 경계값 | [JaCoCo HTML 리포트](build/reports/jacoco/test/html/index.html) |
| 실HTTP E2E | `./gradlew e2eTest` | 회원/상품/위시 흐름, 인증 및 권한 검증 | [E2E 테스트 리포트](build/reports/tests/e2eTest/index.html) |
| 커버리지 기준 | `./gradlew check` | 전체 `line 85% / branch 75%`, 핵심 도메인 `line 95% / branch 90%` | [JaCoCo HTML 리포트](build/reports/jacoco/test/html/index.html) |

### IntelliJ JUnit 실행
구성된 테스트 목록을 파악하기 편리한 방식입니다.

| 구분 | 실행 위치 | 실행 대상 | 확인 포인트 |
| --- | --- | --- | --- |
| 단위 테스트 전체 | `src/test/java` 우클릭 → `Run 'All Tests'` | JUnit 5 단위 테스트 전체 | 도메인 규칙, 예외 처리, 권한 분기, 경계값 |
| 실HTTP E2E 전체 | `src/e2eTest/java` 우클릭 → `Run 'All Tests'` | JUnit 5 + Spring Boot 실HTTP E2E 전체 | 회원/상품/위시 흐름, 인증 및 권한 검증 |
| 단위 테스트 개별 실행 | 테스트 클래스 또는 메서드 우클릭 → `Run` | 원하는 JUnit 클래스/메서드 | IntelliJ JUnit 트리에서 기능 단위 그룹 확인 |
| 커버리지 확인 | 테스트 클래스/패키지 우클릭 → `Run '... with Coverage'` | IntelliJ JUnit Coverage | 빠른 로컬 확인용, 공식 기준 검증은 `./gradlew check` 권장 |

대표 E2E 시나리오는 Gradle과 IntelliJ JUnit 둘 다 아래 테스트에서 바로 실행할 수 있습니다.

| 시나리오 | 테스트 링크 |
| --- | --- |
| 회원 가입, 로그인, refresh 흐름 | [AuthMemberE2eTest](src/e2eTest/java/shopping/e2e/AuthMemberE2eTest.java) |
| 상품 조회, 등록, 수정, 삭제 흐름 | [ProductE2eTest](src/e2eTest/java/shopping/e2e/ProductE2eTest.java) |
| 위시 추가, 조회, 삭제 흐름 | [WishE2eTest](src/e2eTest/java/shopping/e2e/WishE2eTest.java) |
| 판매자 등록부터 사용자 위시 조회까지 핵심 흐름 | [ShoppingFlowE2eTest](src/e2eTest/java/shopping/e2e/ShoppingFlowE2eTest.java) |
