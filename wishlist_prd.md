# 위시 리스트 기능 개발 계획서

## 기능 목록

- [x] 위시 리스트 상품 추가 `POST /api/wishes`
- [x] 위시 리스트 상품 삭제 `DELETE /api/wishes/{wishId}`
- [x] 위시 리스트 상품 조회 `GET /api/wishes`

> 모든 요청에 `Authorization: Bearer {token}` 헤더 필요

---

## 도메인 규칙

| 필드 | 타입 | 제약 |
|------|------|------|
| id | Long | PK, Auto Increment |
| memberId | Long | Not Null |
| productId | Long | Not Null |

- 인증된 회원만 위시 리스트에 접근 가능
- 존재하지 않는 상품은 추가 불가 (400 Bad Request)
- 본인의 위시 항목만 삭제 가능, 존재하지 않는 위시 삭제 시 400 Bad Request

---

## 인증 처리 (신규 구현)

> `member_prd.md`에서 미구현으로 남겨둔 항목 (`AuthArgumentResolver`) 이 단계에서 구현

### `@LoginMember` 어노테이션

- 컨트롤러 파라미터에 인증된 `Member` 객체를 주입하는 마커 어노테이션

### `AuthArgumentResolver`

- `HandlerMethodArgumentResolver` 구현
- `@LoginMember` 파라미터 감지
- `Authorization` 헤더에서 `Bearer {token}` 추출
- `AuthService.getMemberId(token)` 으로 회원 ID 파싱
- `MemberRepository.findById(memberId)` 로 `Member` 조회 후 반환
- 헤더 없거나 토큰 유효하지 않으면 `UnauthorizedException` → 401 반환

### `WebMvcConfig`

- `WebMvcConfigurer` 구현체
- `AuthArgumentResolver` 를 `addArgumentResolvers` 에 등록

---

## 패키지 구조

```
shopping/
├── auth/
│   ├── AuthService.java             # 기존
│   ├── AuthArgumentResolver.java    # 신규
│   └── LoginMember.java             # 신규 (어노테이션)
├── config/
│   └── WebMvcConfig.java            # 신규 (ArgumentResolver 등록)
└── wish/
    ├── api/
    │   ├── WishController.java
    │   └── dto/
    │       ├── WishAddRequest.java
    │       ├── WishAddResponse.java
    │       └── WishResponse.java
    ├── service/
    │   ├── WishCommandService.java
    │   ├── WishQueryService.java
    │   └── dto/
    │       ├── WishAddInput.java
    │       ├── WishAddOutput.java
    │       └── WishOutput.java
    ├── domain/
    │   └── Wish.java
    └── repository/
        └── WishRepository.java
```

---

## DB 스키마

```sql
CREATE TABLE wishes (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (member_id)  REFERENCES members(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## API 명세

### 위시 리스트 상품 추가

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

### 위시 리스트 상품 삭제

```
DELETE /api/wishes/{wishId}
Authorization: Bearer {token}

Response 204
```

### 위시 리스트 상품 조회

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

## 구현 Phase

### Phase 1 - 인증 처리

- [x] `@LoginMember` 어노테이션 작성
- [x] `AuthArgumentResolver` 작성
  - `Authorization` 헤더 추출 및 `Bearer ` 제거
  - `AuthService.getMemberId(token)` 호출
  - `MemberRepository.findById()` 로 Member 반환
  - 헤더 없거나 토큰 파싱 실패 시 `UnauthorizedException` throw
- [x] `WebMvcConfig` 작성 (`AuthArgumentResolver` 등록)
- [x] `AuthArgumentResolver` 단위 테스트 → WishControllerTest에서 통합 검증

### Phase 2 - 도메인 & 영속성

- [x] `Wish` 엔티티 작성
  - 연관관계 없이 `memberId`, `productId` Long 필드로 저장
- [x] `WishRepository` 작성
  - `findByMemberId(Long memberId): List<Wish>`
  - `findByIdAndMemberId(Long id, Long memberId): Optional<Wish>`
- [x] `Wish` 엔티티 단위 테스트 → 생략 (구조 단순)

### Phase 3 - 위시 추가 (DTO + 서비스)

- [x] `WishAddRequest` 레코드 작성 (`productId`) → Phase 6에서 작성
- [x] `WishAddInput` 레코드 작성 (서비스 입력: `memberId`, `productId`)
- [x] `WishAddOutput` 레코드 작성 (서비스 출력: `id`, `productId`, `productName`)
- [x] `WishAddResponse` 레코드 작성 (`id`, `productId`, `productName`) → Phase 6에서 작성
- [x] `WishCommandService.add` 작성
  - `ProductQueryService.getProduct(productId)` 로 상품 조회 (없으면 400)
  - `createWish` 메서드로 분리 후 저장 및 반환

### Phase 4 - 위시 삭제 (서비스)

- [x] `WishCommandService.delete` 작성
  - `delete(Long wishId, Long memberId)`
  - `findByIdAndMemberId` 로 조회 (없거나 본인 것 아니면 400)
  - 삭제

### Phase 5 - 위시 조회 (DTO + 서비스)

- [x] `WishOutput` 레코드 작성 (서비스 출력: `id`, `productId`, `productName`, `price`, `imageUrl`)
- [x] `WishResponse` 레코드 작성 (`id`, `productId`, `productName`, `price`, `imageUrl`)
- [x] `WishQueryService.findWishWithPage` 작성
  - `findWishWithPage(Long memberId, Pageable): Page<WishOutput>`
  - `findByMemberId` 로 조회 후 반환

### Phase 6 - 컨트롤러 & 테스트

- [x] `WishController` 작성
  - `POST /api/wishes` → 201
  - `DELETE /api/wishes/{wishId}` → 204
  - `GET /api/wishes` → 200
  - 파라미터에 `@LoginMember Member member` 사용 (memberId만 서비스로 전달)
- [x] `WishController` 통합 테스트 (MockMvc)

---

## 테스트 케이스

### AuthArgumentResolverTest

| 케이스 | 기대 결과 |
|--------|-----------|
| 유효한 토큰 헤더 | Member 반환 |
| Authorization 헤더 없음 | UnauthorizedException |
| 유효하지 않은 토큰 | UnauthorizedException (401) |

### WishCommandServiceTest

| 케이스 | 기대 결과 |
|--------|-----------|
| 정상 위시 추가 | WishAddOutput 반환 |
| 존재하지 않는 상품 추가 | 예외 발생 (400) |
| 정상 위시 삭제 | 정상 처리 |
| 존재하지 않는 위시 삭제 | 예외 발생 (400) |
| 타인의 위시 삭제 | 예외 발생 (400) |

### WishQueryServiceTest

| 케이스 | 기대 결과 |
|--------|-----------|
| 위시 목록 조회 | WishOutput 리스트 반환 |
| 위시 없는 회원 조회 | 빈 리스트 반환 |

### WishControllerTest

| 케이스 | 기대 결과 |
|--------|-----------|
| 토큰 없이 추가 요청 | 401 |
| 유효 토큰으로 추가 성공 | 201 + WishAddResponse |
| 존재하지 않는 상품 추가 | 400 + message |
| 유효 토큰으로 삭제 성공 | 204 |
| 존재하지 않는 위시 삭제 | 400 + message |
| 유효 토큰으로 목록 조회 | 200 + WishResponse 리스트 |
| 토큰 없이 목록 조회 | 401 |

---

## 주요 결정 사항

| 항목 | 내용 |
|------|------|
| 인증 방식 | `AuthArgumentResolver` + `@LoginMember` (Spring Security 미사용) |
| 토큰 추출 위치 | `Authorization: Bearer {token}` 헤더 |
| Member 주입 방식 | `HandlerMethodArgumentResolver` 로 컨트롤러 파라미터에 직접 주입 |
| 위시 삭제 권한 검증 | `findByIdAndMemberId` 로 본인 소유 확인 |
| Wish-Product 관계 | 연관관계 없이 `memberId`, `productId` Long 필드로 저장 (예상치 못한 버그 방지) |
| 서비스 분리 | `WishCommandService` / `WishQueryService` (Product 패턴과 동일) |
| 입출력 DTO | Input/Output 레코드 분리 (Member/Product 패턴과 동일) |

---

## 구현 체크리스트

### 인증 처리
- [x] `@LoginMember` 어노테이션 작성
- [x] `AuthArgumentResolver` 작성 및 테스트
- [x] `WebMvcConfig` 에 `AuthArgumentResolver` 등록

### 위시 리스트
- [x] `Wish` 엔티티 작성
- [x] `WishRepository` 작성
- [x] `WishCommandService` 작성 및 테스트
- [x] `WishQueryService` 작성 및 테스트
- [x] `WishController` 작성 및 테스트

### 예외 처리
- [x] 인증 없이 위시 접근 시 401 반환
- [x] 존재하지 않는 상품 추가 시 400 반환
- [x] 존재하지 않는 위시 삭제 시 400 반환
