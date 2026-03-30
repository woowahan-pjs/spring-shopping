# Spring Shopping

상품, 회원, 위시리스트 기능을 제공하는 Spring Boot 애플리케이션

## 기술 스택

- Java 21, Spring Boot 3.5.9, Gradle (Kotlin DSL)
- MySQL 8.0 (상품), MongoDB 7.0 (회원 + 위시)
- Flyway (DB 마이그레이션)
- JWT (JJWT 0.12.6) 인증
- Spring Security Crypto (BCrypt)
- Thymeleaf (프론트엔드)
- Cucumber 7.20.1 (BDD 테스트) + Spring REST Docs (API 문서)

## 패키지 구조

```
shopping/
├── product/          # 상품 (도메인, 서비스, 컨트롤러, DTO)
├── member/           # 회원 (도메인, 서비스, 컨트롤러, DTO)
├── wish/             # 위시리스트 (서비스, 컨트롤러, DTO)
├── auth/             # 인증 (JWT 필터)
├── config/           # Spring 설정, 글로벌 예외 처리
├── idempotency/      # 멱등성 필터
├── health/           # 헬스체크
└── page/             # Thymeleaf 페이지
```

## API

### 상품

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/products` | 상품 생성 | O (+ Idempotency-Key) |
| GET | `/api/products` | 상품 목록 조회 | X |
| GET | `/api/products/{id}` | 상품 단건 조회 | X |
| PUT | `/api/products/{id}` | 상품 수정 | O |
| DELETE | `/api/products/{id}` | 상품 삭제 | O |

- 상품 이름: 공백 포함 15자 이하, 특수문자는 `( ) [ ] + - &` 만 허용
- [PurgoMalum API](https://www.purgomalum.com/)로 비속어 검사 (타임아웃 시 graceful degradation)
- 상품 생성 시 `Idempotency-Key` 헤더 필수 (중복 요청 방지)

### 회원

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/members/register` | 회원가입 (이메일 + 비밀번호) |
| POST | `/api/members/login` | 로그인 (JWT 토큰 발급) |

- 이메일 고유, 비밀번호 8자 이상, BCrypt 해싱

### 위시리스트

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/api/wishes/{productId}` | 위시 추가 | O |
| GET | `/api/wishes` | 위시 목록 조회 | O |
| DELETE | `/api/wishes/{productId}` | 위시 삭제 | O |

- 상품 삭제 시 전체 회원의 위시리스트에서 자동 제거

### 헬스체크

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/health/liveness` | Liveness probe |
| GET | `/health/readiness` | Readiness probe (MySQL 연결 확인) |

## 로컬 실행

```bash
docker compose up -d          # MySQL 8.0 + MongoDB 7.0 실행
./gradlew bootRun             # 애플리케이션 실행
./setup.sh                    # 테스트 계정 + 샘플 데이터 생성
```

테스트 계정: `user@test.com` / `password1234`

## 빌드 & 테스트

```bash
./gradlew build               # 빌드 + 테스트 + spotless 검사
./gradlew test                # Cucumber BDD 테스트
./gradlew spotlessApply       # Google Java Style 자동 포맷
./gradlew asciidoctor         # REST API 문서 생성 (build/docs/asciidoc/)
```
