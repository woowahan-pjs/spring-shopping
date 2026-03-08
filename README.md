## 기능 목록

### 상품

- [x] 상품 조회 API 구현
- [x] 상품 단건 조회 API 구현
- [x] 상품 등록 API 구현
- [x] 상품 수정 API 구현
- [x] 상품 삭제 API 구현
- [x] 상품 이름 유효성 검사
- [x] 상품 가격 유효성 검사
- [x] PurgoMalum API 연동하여 비속어 검사

### 회원 기능

- [x] 회원 가입 API 구현
- [x] 로그인 API 구현
- [x] 인증 토큰 발급 기능 구현

### 위시 리스트

- [x] 상품 추가
- [x] 상품 삭제
- [x] 상품 목록 조회

---
## 시스템 규모 추정
MAU = 1천만 가정  
DAU = 100만 (MAU의 10%로 가정).  
일반적으로 10~20% 정도로 잡기 때문에 10%로 설정했습니다.

쇼핑 서비스에서 일반 회원의 행동 흐름은 다음과 같다고 가정합니다.

상품 조회(Read) → 위시리스트 추가(Write) → 위시리스트 조회(Read)

그러면 조회 2회, 쓰기 1회로 총 3회의 요청이 발생합니다.

### QPD
QPD(Query Per Day) = 하루에 발생하는 총 쿼리 수  
DAU에 세션당 쿼리 수를 곱하면 QPD가 계산됩니다.

QPD = DAU(100만) × 세션당 쿼리 수(3회) = 300만
- 세션당 쿼리 수 = 3회
- 상품 조회(Read) → 위시리스트 추가(Write) → 위시리스트 조회(Read) 시나리오 가정
- 조회 2회, 쓰기 1회

일반적으로 읽기/쓰기 작업 비율을 2:1이라고 가정하겠습니다.  
DAU 100만은 하루 기준이므로,

QPD가 300만이므로  
읽기 : 쓰기 = 2 : 1 → 읽기 200만, 쓰기 100만입니다.

### QPS(Query Per Second)  
- **QPS는 읽기 요청 기준**입니다.
- 하루의 초 = 60 × 60 × 24 = 86,400초
- 200만 / 86,400 = 23.1 = (하루 읽기 200만 / 86,400초) × 1.2 = 27.7

### 정리
- 하루 동안 읽기 요청 200만 발생
- 하루의 초 = 86,400
- Peak 시간대 트래픽 가중치 = 1.2  
  (0시부터 23시 59분까지 트래픽이 일정하지 않기 때문에 1.2배 적용)

※ 1.2배는 추정치이며, 서비스에 따라 1.5배~3배까지 적용되기도 합니다.


### TPS(Transaction Per Second)
- TPS는 쓰기 요청 기준입니다.
- 하루의 초 = 60 × 60 × 24 = 86,400초
- 100만 / 86,400 = 11.5  = (하루 쓰기 100만 / 86,400초) × 1.2 = 13.8

### 정리
- 하루 동안 쓰기 요청 100만 발생
- 하루의 초 = 86,400
- Peak 시간대 트래픽 가중치 = 1.2

### Volume 측정
MySQL 기준 (일반적인 계산 방식)
InnoDB의 추가 메타데이터와 인덱스는 제외하고 계산합니다.

WishList 테이블 기준
(사용자가 하루에 한 번 접속하여 상품을 조회한 뒤, 상품 하나를 위시리스트에 추가한다고 가정)
```text
| 컬럼         | 타입        | Byte |
|-------------|------------|------|
| id          | BIGINT     | 8    |
| member_id   | BIGINT     | 8    |
| product_id  | BIGINT     | 8    |
| quantity    | INT        | 4    |
| created_at  | TIMESTAMP  | 4    |
| updated_at  | TIMESTAMP  | 4    |
```
총 36 Byte
하루 사용자 DAU = 100만
하루 동안 저장되는 데이터

36 Byte × 1,000,000 = 36,000,000 Byte 
36,000,000 / 1024 = 35,156.25 KB
36,000,000 / 1024 / 1024 = 34.33 MB
따라서 하루에 저장되는 데이터의 최소 용량은 약 34.33MB로 추정됩니다. <br>

단, 위 계산은 InnoDB의 row overhead(메타데이터)와 인덱스 크기를 제외한 최소 데이터 크기이며,
실제 저장 용량은 인덱스와 메타데이터로 인해 이보다 더 증가할 수 있습니다.
---


## 구현 전략
- JPA를 사용하지만 데이터베이스 모델링을 먼저 진행한 후 개발을 시작합니다.
- 애플리케이션 아키텍처를 사전에 결정하며, 프로젝트 완성 기간과 향후 보완 가능성을 주요 고려 사항으로 두었습니다.


### 아키텍처
레이어드 아키텍처 + 클린 아키텍처의 장점을 혼합한 구조를 사용했습니다.
예외적으로 엔티티가 도메인 역할을 수행하도록 유연하게 설계했습니다.

개발 초기에는 생산성을 위해 레이어드 아키텍처를 기반으로 구현했습니다.
다만 서비스 클래스가 비대해지는 문제를 방지하기 위해 Application 계층에서 UseCase 단위로 책임을 분리했습니다.

```text
presentation (controller)
        ↓
application (usecase / query)
        ↓
domain (entity / repository)
        ↑
infrastructure (jpa / mongo / external)


의존관계
presentation → Application → Domain
                              ↑
                        Infrastructure
                        
                        
board/
 ├── presentation/
 │    └── BoardController
 │
 ├── application/
 │    ├── command(usecase)/
 │    │    ├── CreateBoardUseCase
 │    │    ├── UpdateBoardUseCase
 │    │    └── DeleteBoardUseCase
 │    │
 │    ├── query/
 │    │    ├── BoardQueryService
 │    │    └── BoardQueryRepository   
 │    │
 │    └── dto/
 │         ├── BoardResponse
 │         └── BoardSearchResponse
 │
 ├── domain/
 │    ├── Board
 │    └── BoardRepository             ← Command 전용
 │
 └── infrastructure/
      └── persistence/
           ├── jpa/
           │    ├── SpringDataBoardJpaRepository
           │    └── BoardRepositoryImpl          ← implements BoardRepository
           │
           └── mongo/
                ├── SpringDataBoardMongoRepository
                └── BoardMongoQueryRepository   ← implements BoardQueryRepository                        
```

### DB 모델링
```sql
CREATE TABLE members
(
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  email      VARCHAR(255) NOT NULL UNIQUE,
  password   VARCHAR(255) NOT NULL,
  role       ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE products
(
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  price      INT          NOT NULL,
  image_url  TEXT         NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted    BOOLEAN   DEFAULT FALSE,
);


CREATE TABLE wishes
(
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  member_id  BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity   INT    NOT NULL DEFAULT 1,
  created_at TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                      CONSTRAINT fk_wish_member
                        FOREIGN KEY (member_id) REFERENCES members(id),

  CONSTRAINT fk_wish_product
    FOREIGN KEY (product_id) REFERENCES products (id),

  UNIQUE KEY uk_member_product (member_id, product_id)
);
```











