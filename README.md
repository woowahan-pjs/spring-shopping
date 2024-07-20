# spring-shopping-product

## 요구사항 정리

### 상품 API

> 상품을 조회, 추가, 수정, 삭제할 수 있는 간단한 HTTP API 를 구현

## 용어 사전

### 상품

| 한글명   | 영문명       | 설명                |
|-------|-----------|-------------------|
| 상품    | Product   | 관리자가 등록해서 판매되는 물건 |
| 식별자   | Id        | 물건의 고유번호          |
| 이름    | Name      | 물건의 이름            |
| 이미지경로 | ImagePath | 물건의 이미지 파일 경로     |
| 수량    | Amount    | 물건의 갯수            |
| 가격    | Price     | 물건의 가격            |

### 회원

| 한글명  | 영문명      | 설명       |
|------|----------|----------|
| 회원   | Member   | 회원       |
| 식별자  | Id       | 회원의 고유번호 |
| 이메일  | Email    | 회원의 이메일  |
| 비밀번호 | Password | 회원의 비밀번호 |

### 위시 리스트

| 한글명   | 영문명      | 설명                   |
|-------|----------|----------------------|
| 위시리스트 | Wishlist | 마음에 드는 상품을 담아 놓는 리스트 |
| 식별자   | Id       | 위시리스트 요소 하나의 고유번호    |
| 상품들   | Products | 담긴 상품들               |
| 회원    | Member   | 상품을 담은 회원            |

## 인수조건

### 상품

- Feature: 상품 등록

> Scenario: 상품을 등록함<br>
> When 상품을 등록하면<br>
> Then 상품 목록 조회 시 등록한 상품을 찾을 수 있다<br>

```java
// POST /products
public class ProductRequest {
    private String name;
    private String imagePath;
    private int amount;
    private long price;
}
```

- Feature: 상품 상세 조회

> Scenario: 상품을 상세를 조회<br>
> Given 상품을 등록하고<br>
> When 상품을 상세 조회하면<br>
> Then 상품의 정보를 조회할 수 있다<br>

```java
// GET /products/{id}
public class ProductResponse {
    private Long id;
    private String name;
    private String imagePath;
    private int amount;
    private long price;
}
```

- Feature: 상품 수정

> Scenario: 상품을 수정함<br>
> Given 상품을 등록하고<br>
> When 상품을 수정하면<br>
> Then 상품 목록 조회 시 수정된 상품을 찾을 수 있다<br>

```java
// PUT /products/{id}
public class ProductRequest {
    private String name;
    private String imagePath;
    private int amount;
    private long price;
}
```

- Feature: 상품 삭제

> Scenario: 상품을 삭제함<br>
> Given 상품을 등록하고<br>
> When 상품을 삭제하면<br>
> Then 상품 목록 조회 시 상품이 제거 되어있다.<br>

```java
// DELETE /products/{id}
```

### 회원

- Feature: 회원 등록

> Scenario: 회원가입 함<br>
> When 회원가입을 하면<br>
> Then 정상처리 된다<br>

```java
// POST /members
public class MemberRequest {
    private String email;
    private String password;
}
```

- Feature: 회원 탈퇴

> Scenario: 회원 탈퇴함<br>
> Given 회원가입을 하고<br>
> When 회원 탈퇴를 하면<br>
> Then 회원이 탈퇴된다.<br>

```java
// DELETE /members/{id}
```

- Feature: 로그인

> Scenario: 회원이 로그인 함<br>
> When email 과 password 으로 로그인 요청시 유효한 회원이면<br>
> Then 토큰을 발급 받을 수 있다

```java
// POST /auth/login
```

### 위시리스트

- Feature: 위시리스트에 상품 추가

> Scenario: 위시리스트에 상품을 추가함<br>
> Given 로그인이 되어있고<br>
> Given 상품이 등록되어 있고<br>
> When 위시리스트에 상품을 추가하면<br>
> Then 위시리스트 목록 조회 시 추가한 상품을 찾을 수 있다<br>

```java
// POST /wishlist
public class WishlistRequest {
    private Long productId;
}
```

- Feature: 위시리스트에 상품 삭제

> Scenario: 위시리스트에 상품을 삭제함<br>
> Given 로그인이 되어있고<br>
> Given 위시리스트에 상품이 추가되어 있고<br>
> When 위시리스트에 상품을 삭제하면<br>
> Then 위시리스트 목록 조회 시 상품이 제거 되어있다.<br>

```java
// DELETE /wishlist/{id}
```
