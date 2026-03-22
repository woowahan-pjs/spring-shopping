# spring-shopping

## 기능 목록
### 상품
- [x] 상품은 이름, 가격, 이미지 URL이 있으며, 상품 이미지는 URL 입력 방식으로 관리한다. 
- [x] 상품을 조회한다.
- [x] 상품을 추가한다.
- [x] 상품을 수정한다.
- [x] 상품을 삭제한다.

### 유효성 검사 및 예외 처리
- [x] 상품을 추가, 수정할 때 입력값의 유효성을 검사하고, 잘못된 경우 적절한 오류 응답을 반환한다.
- [x] 상품 이름은 공백을 포함하여 최대 15자까지 입력 가능하다.
- [x] 상품 이름은 (), [], +, -, &, /, _ 특수문자만 사용 가능하며, 그 외 특수문자는 사용할 수 없다.
- [x] 상품 이름은 비속어가 포함될 수 없다.
  - https://www.purgomalum.com/ 
  - 해당 웹사이트의 API를 사용해 비속어 포함 여부를 파악한다.

### 회원 로그인
- [x] 회원은 이메일과 비밀번호로 가입할 수 있다.
- [x] 로그인 시 이메일과 비밀번호를 검증한 후, 일치하면 인증 토큰을 발급한다.

### 위시 리스트
- [x] 회원은 위시 리스트를 사용할 수 있다.
- [x] 회원은 위시 리스트에 상품을 추가할 수 있다.
- [x] 회원은 위시 리스트 상품을 삭제할 수 있다.

## 구현 전략
### 엔티티 설계
- 엔티티
  - Member
    - id
    - email
    - password
    - createdAt
    - updatedAt
  
  - WishList
    - id
    - member
    - items
    - createdAt
    - updatedAt
  
  - WishListItem
    - id
    - wishList
    - product
    - addedAt
  
  - Product
    - id
    - name
    - price
    - imageUrl
    - createdAt
    - updatedAt
  
- 회원은 위시리스트를 하나 가진다.
  - Member 1 --- 1 WishList
- 위시리스트는 여러 상품을 담을 수 있다.
  - WishList 1 --- N WishListItem
- 하나의 상품은 여러 위시리스트에 담길 수 있다.
  - Product 1 --- N WishListItem

### 엔티티와 API 모델 분리
- 엔티티와 API 모델(DTO)을 분리하여 구현한다.
- 엔티티와 API 모델(DTO)을 분리해 아래와 같은 문제를 방지한다.
  - 보안 문제
    - 엔티티를 그대로 반환하면 password와 같은 값이 그대로 노출된다.
  - API 스펙이 DB 구조에 종속
    - DB 구조 변경 시 API 구조도 같이 변경해야 한다.

### DTO 분리
- Request
- Response

## 테스트 전략
본 프로젝트는 계층별 역할에 맞춰 단위 테스트를 중심으로 작성했습니다.

### Controller 테스트 전략
@WebMvcTest와 MockMvc를 사용하여 웹 계층만 독립적으로 테스트합니다.

  - Service 계층은 @MockitoBean으로 Mock 처리
  - HTTP 요청/응답 흐름 검증에 집중
  - 주요 검증 내용
    - 요청 → 컨트롤러 → 서비스 호출 여부 (verify)
    - 응답 상태 코드 (200, 400 등)
    - JSON 응답 구조 및 값 (jsonPath)
    - 예외 발생 시 에러 응답 형태

즉, **“컨트롤러가 요청을 제대로 받고, 올바른 응답을 반환하는지”**만 검증합니다.(비즈니스 로직은 검증하지 않음)

### Service 테스트 전략
Mockito 기반 단위 테스트로 비즈니스 로직을 집중 검증합니다.

- Repository, 외부 의존성(JWT, Encoder 등)은 Mock 처리
- 도메인 객체는 실제 객체를 사용하여 로직 검증
- 주요 검증 내용
  - 비즈니스 로직 정상 동작 여부
  - 상태 변화 (값 변경, 생성 등)
  - 예외 상황 처리 (assertThrows)
  - 의존 객체 호출 여부 (verify)
즉, **“서비스의 핵심 로직이 올바르게 동작하는지”**를 검증합니다.

### Repository 테스트 전략
@DataJpaTest를 사용하여 실제 DB(JPA) 동작을 검증합니다.

- 인메모리 DB 기반 테스트 수행
- 실제 엔티티 저장/조회/삭제 검증
- 주요 검증 내용
  - CRUD 동작 확인
  - 쿼리 메서드 동작 (findByEmail, findByWishListId 등)
  - 영속성 컨텍스트 동작

즉, **“DB와의 상호작용이 정상적으로 수행되는지”**를 검증합니다.

## AI 활용 내용 기록
### 활용 방식
- ChatGPT를 활용하여 설계 방향, 구현 방법, 테스트 코드 작성 방식 등에 대한 설명을 참고하였다.
- 생성된 코드를 그대로 사용하지 않고 프로젝트 구조와 요구사항에 맞게 수정하여 적용하였다.
- 기능 구현 및 테스트 과정에서 발생한 오류 원인 분석 및 해결 방법을 참고하였다.

### 2026.03.03 ~ 2026.03.04
  - JPA 엔티티 설계에 대한 설명을 참고하였다.
  - Wishlist와 WishlistItem 관계 설계 아이디어를 얻었다.
  - 생성된 코드는 그대로 사용하지 않고 프로젝트 구조에 맞게 수정하였다.
### 2026.03.05 ~ 2026.03.06
  - 상품 엔티티 작성 시 개선 사항에 대한 설명을 참고하였다.
  - 코드 작성 전 패키지 구조를 어떻게 만들지에 관한 설명을 참고하였다.
  - 요구사항 구현을 위한 상품 이름 유효성 검사 및 예외처리 방법에 대한 설명을 참고하였다.
### 2026.03.07
  - 상품 이름 유효성 검사 및 예외 처리에 관한 실무에서 많이 쓰는 코드를 참고하였다.
### 2026.03.08
  - 회원가입 및 로그인 API 구현 과정에서 Controller, Service, Repository 구조 설계 방법을 참고하였다.
  - JWT 기반 인증 처리 흐름과 토큰 생성 방식에 대한 설명을 참고하였다.
  - 회원가입 및 로그인 기능에 대한 테스트 코드(MockMvc 기반 Controller 테스트) 작성 방법을 참고하였다.
  - Gradle 의존성 오류 및 테스트 환경 설정 문제 해결 방법을 참고하였다.
### 2026.03.09
  - 위시리스트 기능 구현 과정에서 Wishlist 및 WishlistItem 구조 설계에 대한 설명을 참고하였다.
  - 위시리스트 상품 추가 및 조회 기능 구현 방법을 참고하였다.
  - Repository 테스트 코드 작성 시 EntityManager flush/clear 사용 이유에 대한 설명을 참고하였다.
  - Spring Security 개념 및 테스트 시 발생한 인증 오류 해결 방법을 참고하였다.
### 2026.03.10
  - 상품 이름 비속어 검증 기능 구현을 위해 외부 API(PurgoMalum) 활용 방법을 참고하였다.
  - 외부 API 호출 로직에 대한 테스트 코드 작성 방법을 참고하였다.

### 기록 1
#### 무엇을 학습했는지
- JPA 엔티티 라이프사이클 콜백 어노테이션
  - JPA 사용 시 DB insert 및 update 시점을 잡아 로직을 작성할 수 있음을 알게 되었다.
    - @PrePersist
      - insert 전 실행
    - @PreUpdate
      - update 전 실행
#### 코드 수정 내용
###### 수정 전
```java
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected Product() {

    }

    public Product(String name, int stockQuantity, long price, String imageUrl) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }
```
##### 수정 후
```java

  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
  
  protected Product() {
  
  }
   @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Product(String name, int stockQuantity, long price, String imageUrl) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }
```

### 기록 2
#### 무엇을 학습했는지
- static factory method
  - Product 엔티티에 생성자를 추가하려 할 때 생성자 추가보다 static factory method를 활용하는 방법이 있으며, 이로 인한 장점을 알게 되었다.
    - 장점
      - 메소드명을 통해 생성 의도를 나타낼 수 있다.
        - Product.createForSale(name, price)
        - Product.createForStock(name, price, stockQuantity)
      - 생성 방식 변경 가능 -> 요구사항 변경 시 서비스 코드를 수정하지 않을 수 있다.
        - 요구사항 변경 전
          ```java
            public static Product create(String name, long price) {
                return new Product(name, 0, price, null);
            }
          ```
         - 요구사항 변경 후 
          ```java
           public static Product create(String name, long price) {
                return new Product(name, 10, price, DEFAULT_IMAGE);
            }
          ```
#### 코드 수정 내용
##### 수정 전
```java
   public Product(String name, int stockQuantity, long price, String imageUrl) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }
```
##### 수정 후
```java
   public Product(String name, int stockQuantity, long price, String imageUrl) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product create(String name, long price) {
        return new Product(name, 0, price, null);
    }
```
