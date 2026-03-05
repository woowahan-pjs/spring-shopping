# spring-shopping

## 기능 목록
### 상품
- [] 상품은 이름, 가격, 이미지 URL이 있으며, 상품 이미지는 URL 입력 방식으로 관리한다. 
- [] 상품을 조회한다.
- [] 상품을 추가한다.
- [] 상품을 수정한다.
- [] 상품을 삭제한다.

### 유효성 검사 및 예외 처리
- [] 상품을 추가, 수정할 때 입력값의 유효성을 검사하고, 잘못된 경우 적절한 오류 응답을 반환한다.
- [] 상품 이름은 공백을 포함하여 최대 15자까지 입력 가능하다.
- [] 상품 이름은 (), [], +, -, &, /, _ 특수문자만 사용 가능하며, 그 외 특수문자는 사용할 수 없다.
- [] 상품 이름은 비속어가 포함될 수 없다.
  - https://www.purgomalum.com/ 
  - 해당 웹사이트의 API를 사용해 비속어 포함 여부를 파악한다.

### 회원 로그인
- [] 회원은 이메일과 비밀번호로 가입할 수 있다.
- [] 로그인 시 이메일과 비밀번호를 검증한 후, 일치하면 인증 토큰을 발급한다.

### 위시 리스트
- [] 회원은 위시 리스트를 사용할 수 있다.
- [] 회원은 위시 리스트에 상품을 추가할 수 있다.
- [] 회원은 위시 리스트 상품을 삭제할 수 있다.

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

## AI 활용 내용 기록
### 활용 방식
### 코드 수정 내용
### 무엇을 학습했는지

