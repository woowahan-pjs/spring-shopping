# spring-shopping

## 상품
상품을 조회, 추가, 수정, 삭제하는 기능을 구현한다.

* [ ] /api/products POST 상품 생성 새 상품을 등록한다.
* [ ] /api/products/{productId} GET 상품 조회 특정 상품의 정보를 조회한다.
* [ ] /api/products/{productId} PUT 상품 수정기존 상품의 정보를 수정한다.
* [ ] /api/products/{productId} DELETE 상품 삭제 특정 상품을 삭제한다.
* [ ] /api/products GET 상품 목록 조회 모든 상품의 목록을 조회한다.

### 상품속성
* 이름
  * 공백포함 15자이다.
  * 특수문자는 괄호, +, - 만 허용한다.
  * PurgoMalum API 로 비속어 검사한다. (https://www.purgomalum.com/)
    * https://www.purgomalum.com/service/xml?text=this is some test input
* 가격
* 이미지URL (파일업로드는 아직 구현하지 않는다)

## 회원
로그인은 이메일과 비밀번호로 가입한다
인증 토큰을 발급한다
[ ] /api/members/register POST 회원가입 
    * 이메일과 비밀번호로 회원가입한다. 
    * 이메일은 고유해야 한다. 
    * 비밀번호는 8자 이상이어야 한다. 
    * 회원가입이 성공하면 토큰을 발급한다.
[ ] /api/members/login POST 로그인 회원

# 구현 전략

* 상품, 회원은 각각 별도의 모듈로 구현한다
  * 상품 worktree, 회원 worktree 로 병렬로 개발한다
  * 각 모듈엔 spring dependency 를 넣지 않고 최대한 pojo 로 구현한다
  * spring dependency 가 필요한 부분은 메인으로 가져온다
  * 현재 stage 에선 디비는 고려하지 않고 in memory repository 로 구현한다
* ai 로 동작하는 코드를 먼저 생성한다.
  * 구현하면 pr 을 만들도록 하여 리뷰를 통해 머지한다
  * 테스트코드를 작성한다
* 기능개발과 별도의 리팩토링 스텝을 갖는다

# 작업 순서
1. 디비 종속성없이 상품과 회원 도메인 모델을 구현한다
2. 상품과 회원 기능에 대한 ai 구현코드를 작성한다
3. 상품과 회원 도메인 모델에 대한 ai 테스트코드를 작성한다
4. 상품과 회원 기능에 대한 ai 구현코드를 리팩토링한다
5. 적당한 디비를 선택해서 추가한다

# 기능 구현 세부사항
* [x] 패스워드는 평문으로 저장되지 않게 한다
* [ ] 헬스체크를 추가한다
* 