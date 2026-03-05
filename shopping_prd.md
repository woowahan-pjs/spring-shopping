# 과제4 개발 계획서

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
- [x] 전역 예외 핸들러 구현 (`@RestControllerAdvice`)
- [x] 유효성 검사 실패 시 400 Bad Request 응답

---

### 3. 회원 (Member)

- [x] 회원 가입 `POST /api/members/register`
  - 이메일 + 비밀번호로 가입
  - 가입 성공 시 인증 토큰 발급
- [x] 로그인 `POST /api/members/login`
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
├── product/
│   ├── controller/
│   │   └── ProductController.java
│   ├── service/
│   │   └── ProductService.java
│   ├── domain/
│   │   └── Product.java
│   ├── repository/
│   │   └── ProductRepository.java
│   └── dto/
│       ├── ProductRequest.java
│       └── ProductResponse.java
├── member/
│   ├── controller/
│   │   └── MemberController.java
│   ├── service/
│   │   └── MemberService.java
│   ├── domain/
│   │   └── Member.java
│   ├── repository/
│   │   └── MemberRepository.java
│   └── dto/
│       ├── MemberRequest.java
│       └── TokenResponse.java
├── wish/
│   ├── controller/
│   │   └── WishController.java
│   ├── service/
│   │   └── WishService.java
│   ├── domain/
│   │   └── Wish.java
│   ├── repository/
│   │   └── WishRepository.java
│   └── dto/
│       ├── WishRequest.java
│       └── WishResponse.java
├── auth/
│   ├── AuthService.java           # 토큰 검증 및 회원 식별
│   └── AuthArgumentResolver.java  # @LoginMember 파라미터 처리
└── global/
    ├── exception/
    │   ├── GlobalExceptionHandler.java
    │   └── ErrorResponse.java
    └── client/
        └── PurgoMalumClient.java   # 비속어 검사 외부 API 클라이언트
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

- [x] Google Java Style Guide 준수 (들여쓰기 4 spaces)
- [x] 들여쓰기 최대 2단계
- [x] 함수 최대 15줄
- [x] 함수는 한 가지 일만 수행
- [x] `else` 키워드 사용 금지
- [x] `switch` 문 사용 금지
- [x] 3항 연산자 사용 금지
- [x] JUnit 5 + AssertJ로 기능 테스트 작성