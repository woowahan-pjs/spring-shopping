# spring-shopping

## 상품
상품을 조회, 추가, 수정, 삭제하는 기능을 구현한다.
/api/products POST 상품 생성 새 상품을 등록한다.
/api/products/{productId} GET 상품 조회 특정 상품의 정보를 조회한다.
/api/products/{productId} PUT 상품 수정기존 상품의 정보를 수정한다.
/api/products/{productId} DELETE 상품 삭제 특정 상품을 삭제한다.
/api/products GET 상품 목록 조회 모든 상품의 목록을 조회한다.
### 상품속성
이름
* 공백포함 15자이다.
* 특수문자는 괄호, +, - 만 허용한다.
* PurgoMalum API 로 비속어 검사한다. (https://www.purgomalum.com/)
  * https://www.purgomalum.com/service/xml?text=this is some test input
가격
이미지URL (파일업로드는 아직 구현하지 않는다)

## 회원
로그인은 이메일과 비밀번호로 가입한다
인증 토큰을 발급한다
/api/members/register POST 회원가입 이메일과 비밀번호로 회원가입한다. 이메일은 고유해야 한다. 비밀번호는 8자 이상이어야 한다. 회원가입이 성공하면 토큰을 발급한다.
/api/members/login POST 로그인 회원

# 그외 구현
상품, 회원은 각각 모듈로 별도로 구현한다