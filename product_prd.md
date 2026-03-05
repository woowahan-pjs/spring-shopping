# 상품 기능 개발 계획서

## 기능 목록

- [ ] 상품 생성 `POST /api/products`
- [ ] 상품 단건 조회 `GET /api/products/{productId}`
- [ ] 상품 목록 조회 `GET /api/products`
- [ ] 상품 수정 `PUT /api/products/{productId}`
- [ ] 상품 삭제 `DELETE /api/products/{productId}`

---

## 도메인 규칙

| 필드 | 타입 | 제약 |
|------|------|------|
| name | String | 공백 포함 최대 15자, 허용 특수문자만 사용 가능 |
| price | Long | 양수 |
| imageUrl | String | URL 형식 |

**허용 특수문자:** `( )`, `[ ]`, `+`, `-`, `&`, `/`, `_`

**비속어 검사:** PurgoMalum API 호출로 상품명 비속어 포함 여부 확인

---

## 유효성 검사

- [ ] 상품명 길이 검사 (공백 포함 최대 15자)
- [ ] 상품명 허용 특수문자 검사 (정규식: `^[a-zA-Z0-9가-힣 ()\[\]+\-&/_]*$`)
- [ ] 상품명 비속어 검사 (PurgoMalum API 연동)
- [ ] 가격 양수 검사

---

## 패키지 구조

```
shopping/product/
├── api/
│   ├── ProductController.java
│   └── dto/
│       ├── ProductRequest.java
│       └── ProductResponse.java
├── service/
│   └── ProductService.java
├── domain/
│   └── Product.java
└── repository/
    └── ProductRepository.java

shopping/common/client/
└── PurgoMalumClient.java
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
```

---

## API 명세

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

## 구현 Phase

### Phase 1 - 도메인 & 영속성 ✅
- [x] `Product` 엔티티 작성
- [x] `ProductRepository` 작성
- [x] 엔티티 단위 테스트

### Phase 2 - DTO ✅
- [x] `ProductRequest` 레코드 작성
- [x] `ProductResponse` 레코드 작성

### Phase 3 - 외부 API 클라이언트 ✅
- [x] `PurgoMalumValidator` 작성 (`RestTemplate` 기반)
- [x] 비속어 포함 시 `true` 반환 단위 테스트
- [ ] `RestTemplate` 빈 등록 설정 (`RestTemplateConfig`)

### Phase 4 - 서비스 ✅
- [x] `ProductService` 상품 생성 구현 + 테스트
  - 상품명 길이 검사
  - 상품명 허용 특수문자 검사
  - 상품명 비속어 검사
  - 가격 양수 검사
- [x] `ProductService` 상품 단건 조회 구현 + 테스트
- [x] `ProductService` 상품 목록 조회 구현 + 테스트
- [x] `ProductService` 상품 수정 구현 + 테스트
- [x] `ProductService` 상품 삭제 구현 + 테스트

### Phase 5 - 컨트롤러
- [ ] `ProductController` 작성
- [ ] 컨트롤러 통합 테스트 (MockMvc)
