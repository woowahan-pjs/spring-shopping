# spring-shopping

## 구현 기능 목록

### 회원 API

- [x] `POST /api/members/register` — 회원가입
- [x] `POST /api/members/login` — 로그인

### 상품 API

- [x] `POST /api/products` — 상품 생성
- [x] `GET /api/products/{productId}` — 상품 조회
- [x] `PUT /api/products/{productId}` — 상품 수정
- [x] `DELETE /api/products/{productId}` — 상품 삭제
- [x] `GET /api/products` — 상품 목록 조회 (페이징)
- [x] 상품 이름 유효성 검사 (최대 15자, 허용 특수문자: `( ) [ ] & , - + / _`)
- [x] 상품 이름 비속어 검사 (비속어 포함시 예외)

### 위시리스트 API

- [x] `POST /api/wishes` — 위시리스트 상품 추가
- [x] `DELETE /api/wishes/{wishId}` — 위시리스트 상품 삭제
- [x] `GET /api/wishes` — 위시리스트 상품 조회
