# spring-shopping


## 프로젝트 스택

- Language: Java 21
- Framework: Spring Boot 3.5.9
- ORM: Spring Data JPA
- DB: H2
- Build: Gradle
- Auth: JWT (jjwt)
- Test: JUnit 5 + AssertJ

---

## 기능 목록

### 1. 상품 (Product)

- [ ] 상품 생성 `POST /api/products`
- [ ] 상품 단건 조회 `GET /api/products/{productId}`
- [ ] 상품 목록 조회 `GET /api/products`
- [ ] 상품 수정 `PUT /api/products/{productId}`
- [ ] 상품 삭제 `DELETE /api/products/{productId}`

#### 상품 도메인 규칙

| 필드 | 타입 | 제약 |
|------|------|------|
| name | String | 공백 포함 최대 15자, 허용 특수문자만 사용 가능 |
| price | Long | 양수 |
| imageUrl | String | URL 형식 |

**허용 특수문자:** `( )`, `[ ]`, `+`, `-`, `&`, `/`, `_`

**비속어 검사:** PurgoMalum API 호출로 상품명 비속어 포함 여부 확인

---

### 2. 유효성 검사 및 예외 처리

- [ ] 상품명 길이 검사 (공백 포함 최대 15자)
- [ ] 상품명 허용 특수문자 검사 (정규식 기반)
- [ ] 상품명 비속어 검사 (PurgoMalum API 연동)
- [ ] 전역 예외 핸들러 구현 (`@RestControllerAdvice`)
- [ ] 유효성 검사 실패 시 400 Bad Request 응답

---

### 3. 회원 (Member)

- [ ] 회원 가입 `POST /api/members/register`
    - 이메일 + 비밀번호로 가입
    - 가입 성공 시 인증 토큰 발급
- [ ] 로그인 `POST /api/members/login`
    - 이메일 + 비밀번호 검증
    - 일치 시 인증 토큰 발급

**토큰 전략:** JWT (회원 ID를 subject로 담아 서명, 서버 무상태 유지)

---

### 4. 위시 리스트 (Wishlist)

> 인증된 사용자만 접근 가능

- [ ] 위시 리스트 상품 추가 `POST /api/wishes`
- [ ] 위시 리스트 상품 삭제 `DELETE /api/wishes/{wishId}`
- [ ] 위시 리스트 상품 조회 `GET /api/wishes`

**인증 방식:** 요청 헤더 `Authorization: Bearer {token}` → 토큰으로 회원 식별

---

## API 명세

### 회원 API

#### 회원 가입
```
POST /api/members/register
Content-Type: application/json

Request:
{
  "email": "user@example.com",
  "password": "password123"
}

Response 201:
{
  "token": "uuid-token"
}
```

#### 로그인
```
POST /api/members/login
Content-Type: application/json

Request:
{
  "email": "user@example.com",
  "password": "password123"
}

Response 200:
{
  "token": "uuid-token"
}
```

---

### 상품 API

#### 상품 생성
```
POST /api/products
Content-Type: application/json

Request:
{
  "name": "상품명",
  "price": 10000,
  "imageUrl": "https://example.com/image.jpg"
}

Response 201:
{
  "id": 1,
  "name": "상품명",
  "price": 10000,
  "imageUrl": "https://example.com/image.jpg"
}
```

#### 상품 단건 조회
```
GET /api/products/{productId}

Response 200:
{
  "id": 1,
  "name": "상품명",
  "price": 10000,
  "imageUrl": "https://example.com/image.jpg"
}
```

#### 상품 목록 조회
```
GET /api/products

Response 200:
[
  {
    "id": 1,
    "name": "상품명",
    "price": 10000,
    "imageUrl": "https://example.com/image.jpg"
  }
]
```

#### 상품 수정
```
PUT /api/products/{productId}
Content-Type: application/json

Request:
{
  "name": "수정된 상품명",
  "price": 20000,
  "imageUrl": "https://example.com/new-image.jpg"
}

Response 200:
{
  "id": 1,
  "name": "수정된 상품명",
  "price": 20000,
  "imageUrl": "https://example.com/new-image.jpg"
}
```

#### 상품 삭제
```
DELETE /api/products/{productId}

Response 204
```

---

### 위시 리스트 API

> 모든 요청에 `Authorization: Bearer {token}` 헤더 필요

#### 위시 리스트 상품 추가
```
POST /api/wishes
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "productId": 1
}

Response 201:
{
  "id": 1,
  "productId": 1,
  "productName": "상품명"
}
```

#### 위시 리스트 상품 삭제
```
DELETE /api/wishes/{wishId}
Authorization: Bearer {token}

Response 204
```

#### 위시 리스트 상품 조회
```
GET /api/wishes
Authorization: Bearer {token}

Response 200:
[
  {
    "id": 1,
    "productId": 1,
    "productName": "상품명",
    "price": 10000,
    "imageUrl": "https://example.com/image.jpg"
  }
]
```

---

## 패키지 구조

```
shopping/
├── Application.java
├── auth/
│   └── AuthService.java           # 토큰 생성 및 회원 ID 파싱
├── config/
│   └── JpaConfig.java
├── common/
│   ├── converter/
│   │   ├── EnumType.java
│   │   ├── EnumTypeConvertUtils.java
│   │   └── EnumTypeConverter.java
│   ├── entity/
│   │   └── BaseEntity.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ErrorResponse.java
│       └── UnauthorizedException.java
├── member/
│   ├── api/
│   │   ├── MemberController.java
│   │   └── dto/
│   │       ├── MemberRequest.java
│   │       └── TokenResponse.java
│   ├── service/
│   │   └── MemberService.java
│   ├── domain/
│   │   └── Member.java
│   └── repository/
│       └── MemberRepository.java
├── product/                       # 미구현
│   ├── api/
│   │   ├── ProductController.java
│   │   └── dto/
│   │       ├── ProductRequest.java
│   │       └── ProductResponse.java
│   ├── service/
│   │   └── ProductService.java
│   ├── domain/
│   │   └── Product.java
│   └── repository/
│       └── ProductRepository.java
└── wish/                          # 미구현
    ├── api/
    │   ├── WishController.java
    │   └── dto/
    │       ├── WishRequest.java
    │       └── WishResponse.java
    ├── service/
    │   └── WishService.java
    ├── domain/
    │   └── Wish.java
    └── repository/
        └── WishRepository.java
```

---

## DB 스키마

```sql
CREATE TABLE products (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(15)  NOT NULL,
    price      BIGINT       NOT NULL,
    image_url  VARCHAR(500) NOT NULL
);

CREATE TABLE members (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE wishes (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id)  REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## 구현 전략

### 인증 처리
- `Authorization: Bearer {token}` 헤더에서 JWT 추출
- JWT 서명 검증 후 subject(회원 ID)를 파싱
- `AuthArgumentResolver`에서 파싱한 ID로 Member 조회 후 컨트롤러 파라미터에 주입
- 토큰 없거나 유효하지 않으면 401 Unauthorized 반환

### 비속어 검사 (PurgoMalum API)
- `RestTemplate`으로 `https://www.purgomalum.com/service/containsprofanity?text={name}` 호출
- 응답이 `true`이면 400 Bad Request 반환

### 상품명 유효성 검사
```
정규식: ^[a-zA-Z0-9가-힣 ()\[\]+\-&/_]*$
길이: 1 이상 15 이하
```

### 예외 응답 형식
```json
{
  "message": "에러 메시지"
}
```

---

## 커밋 전략 (AngularJS Commit Convention)

```
feat: 상품 CRUD API 구현
feat: 상품 유효성 검사 및 예외 처리 구현
feat: 회원 가입/로그인 API 구현
feat: 인증 토큰 발급 및 검증 구현
feat: 위시 리스트 API 구현
test: 상품 서비스 단위 테스트 작성
test: 회원 서비스 단위 테스트 작성
test: 위시 리스트 서비스 단위 테스트 작성
```

---

## 프로그래밍 제약 사항 체크리스트

- [ ] Google Java Style Guide 준수 (들여쓰기 4 spaces)
- [ ] 들여쓰기 최대 2단계
- [ ] 함수 최대 15줄
- [ ] 함수는 한 가지 일만 수행
- [ ] `else` 키워드 사용 금지
- [ ] `switch` 문 사용 금지
- [ ] 3항 연산자 사용 금지
- [ ] JUnit 5 + AssertJ로 기능 테스트 작성

---

## 현재 구현 상세

기존 섹션은 요구사항과 설계 의도를 정리한 내용이고, 아래 내용은 현재 코드 기준의 실제 구현 상태를 보완 설명한다.

### 패키지 구조

- `shopping.member`: 회원 가입/로그인 API, 회원 조회/저장
- `shopping.product`: 상품 생성/조회/수정/삭제 API 및 상품 검증
- `shopping.wish`: 위시리스트 추가/조회/삭제 API
- `shopping.auth`: JWT 발급/검증, 인증 사용자 주입
- `shopping.common`: 예외 응답, 공통 엔티티, 외부 클라이언트
- `shopping.config`: JPA Auditing, `RestTemplate`, `PasswordEncoder`, MVC 설정

### 실제 구현된 동작

- 회원 가입 시 이메일 중복을 검사한 뒤 비밀번호를 암호화해서 저장하고 JWT를 즉시 발급한다.
- 로그인 시 이메일로 회원을 조회하고 비밀번호를 검증한 뒤 JWT를 발급한다.
- 상품 API는 인증 없이 호출 가능하다.
- 위시리스트 API는 `Authorization: Bearer {token}` 헤더가 반드시 필요하다.
- 위시리스트 조회 시 연결된 상품이 삭제된 경우, 해당 항목은 `"삭제된 상품입니다."` 문구와 함께 조회된다.

### 인증 및 보안

- 비밀번호는 `Argon2PasswordEncoder`로 암호화한다.
- JWT는 회원 ID를 `subject`로 저장해서 서명하며, 서버는 별도 세션 없이 토큰만으로 사용자를 식별한다.
- `@LoginMember` 파라미터는 `AuthArgumentResolver`가 처리하며, 토큰 검증 후 `Member` 객체를 컨트롤러에 주입한다.
- 토큰이 없으면 `401 Unauthorized`와 `"인증 토큰이 없습니다."`를 반환한다.
- 토큰이 파싱되지 않거나 잘못된 경우 `401 Unauthorized`와 `"유효하지 않은 토큰입니다."`를 반환한다.

### 검증 및 예외 처리

- 상품명 길이 초과, 허용되지 않은 특수문자, 비속어 포함, 가격 0 이하인 경우 `400 Bad Request`를 반환한다.
- 중복 이메일 회원가입, 존재하지 않는 이메일 로그인, 비밀번호 불일치도 `400 Bad Request`로 처리한다.
- 존재하지 않는 상품/위시리스트 접근은 `400 Bad Request`로 처리한다.
- 인증 예외는 `401 Unauthorized`, 그 외 예상하지 못한 예외는 `500 Internal Server Error`로 `{"message": "..."}` 형식으로 응답한다.

### 데이터 저장 방식

- 개발 환경 DB는 H2 in-memory를 사용한다.
- JPA `ddl-auto: create-drop` 설정으로 애플리케이션 시작 시 스키마를 생성하고 종료 시 제거한다.
- `Product`, `Wish`는 모두 소프트 삭제 방식이다. 삭제 요청 시 레코드를 즉시 제거하지 않고 `deleted`, `deletedAt` 값만 변경한다.
- `BaseEntity`와 JPA Auditing으로 생성/수정 시각을 관리한다.

### 비속어 검사 구현 메모

- 상품명 비속어 검사는 `PurgoMalum` 외부 API를 `RestTemplate`으로 호출해서 수행한다.
- `RestTemplate`의 연결/응답 타임아웃은 각각 5초로 설정되어 있다.
- 현재 구현은 외부 서비스 판별 결과에 의존하므로 영어권 비속어는 차단되더라도 한국어 비속어는 차단되지 않을 수 있다.
- 예를 들어 `시발` 같은 단어는 현재 환경에서 상품명 검증을 통과할 수 있다.

### 테스트 및 수동 확인

- 컨트롤러 테스트는 `MockMvc` 기반 통합 테스트로 작성되어 있다.
- `member`, `product`, `wish` 각 도메인에 대해 성공/실패 케이스를 포함한 테스트가 존재한다.
- 수동 API 확인용 HTTP 파일은 `http/controllers.http`에 추가되어 있다.
- 위 파일은 회원 가입/로그인, 상품 생성/조회/수정/삭제, 위시리스트 추가/조회/삭제 순서로 실행하면 된다.
