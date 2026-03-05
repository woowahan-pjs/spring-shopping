온라인쇼핑몰 만들기.

# 진행 요구 사항 
- 기능 목록 만들기
- 기능 단위 커밋
- 요구 사항에 없다면 자유롭게 추가
- 문제 분해 & 명확한 설계하는 훈련.
- AI 도구 사용해도되는데, 단순히 사용하는걸 넘어 이해하고, 어떻게 활용했는지 구체적으로 기록하여 활용한다.

# 기술 스택
- Framework : SpringBoot 
- request : HTTP API 
- response : JSON
- language : Java or Kotlin
- test : Junit 5, AssertJ

## 회원
- 회원 가입
- POST /api/members/register
  - 이메일과 비밀번호
  - 이메일 정규식
  - 비밀번호 단방향 알고리즘
  - 예외처리 빡세게. (이메일과 비밀번호를 구분해서 응답하지 않는다.)
- 로그인
  - POST /api/members/login
  - 인증 토큰 (JWT)
  - 유효시간 30분
  - 서버가 알아서 refresh 처리
  - 클라이언트는 토큰을 신경쓰지 않는다.
- 회원 조회
  - GET /api/members/{memberId}
- 회원 목록 조회
  - GET /api/members

## 상품
- 상품 생성
  - POST /api/products
- 상품 조회
  - GET /api/products/{productId}
- 상품 수정
  - PUT /api/products/{productId}
- 상품 삭제
  - DELETE /api/products/{productId}
- 상품 목록 조회
  - GET /api/products

### 상품 유효성 검사 및 예외 처리
상품을 추가하거나 수정할 때, 클라이언트로부터 전달된 값의 유효성을 검사하고 잘못된 경우 적절한 오류 응답을 반환한다.
- 공백 포함 최대 15자
- 특수문자
  - 허용 : (), [], +, -, &, /, _
- 비속어 처리
  - [PurgoMalum](https://www.purgomalum.com/) API 활용

## 위시 리스트
- 위시 리스트 상품 추가
  - POST /api/wishes
- 위시 리스트 상품 삭제
  - DELETE /api/wishes/{wishId}
- 위시 리스트 상품 조회
  - GET /api/wishes

인증(로그인)된 사용자를 대상으로 기능이 동작한다.

# 프로그래밍 요구사항
자바 코드 컨벤션
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
단, 들여쓰기는 4 spaces.

클린코드, SOLID 원칙을 준수한다
들여쓰기 단계가 3을 넘지 않도록 구현한다. 2까지만 허용한다.
예를 들어, while문 안의 if문은 들여쓰기 2이다.
else도 사용하지 않는다.
switch문도 사용하지 말자.
3항 연산자도 쓰지말자.


