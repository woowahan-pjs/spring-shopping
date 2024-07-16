# spring-shopping-product
## 기능요구사항
### 상품
- [x] 상품 등록 api
  - [x] 공백 포함 15자 검사
  - [x] 특정 특수문자 의외의 특수문자 존재하지 않는지 검사
  - [x] 비속어 검사
- [x] 상품 조회 api
- [x] 상품 수정 api
  - [x] 상품명 형식 검사
- [x] 상품 삭제 api
### 로그인
- [x] 회원가입
  - [x] 이메일 중복 검사
  - [x] 이메일 형식 검사
  - [x] 비밀번호 암호화
  - [x] 비밀번호 형식 검사
- [ ] 로그인
  - [ ] 로그인시 토큰 발급 api(엑세스, 리프레시 토큰)
  - [ ] 엑세스 토큰 재발급 api
### 위시리스트
- [ ] 위시리스트 상품목록 조회 api
- [ ] 위시리스트 상품 추가 api
  - [ ] 중복 추가 방지
  - [ ] 상품 존재 여부 검사
- [ ] 위시리스트 상품 삭제 api
  - [ ] 상품 존재 여부 검사
