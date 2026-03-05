# spring-shopping

## 필수 기능
### 회원 API
- [ ] `POST /api/members/register` 회원 가입
- [ ] `POST /api/members/login` 로그인
- [ ] `GET /api/members/{memberId}` 회원 조회
- [ ] `GET /api/members` 회원 목록 조회

### 상품 API
- [ ] `POST /api/products` 상품 생성
- [ ] `GET /api/products/{productId}` 상품 조회
- [ ] `PUT /api/products/{productId}` 상품 수정
- [ ] `DELETE /api/products/{productId}` 상품 삭제
- [ ] `GET /api/products` 상품 목록 조회

#### 상품 유효성 검사
- [ ] 이름 최대 15자 제한
- [ ] 허용 특수문자만 사용
- [ ] PurgoMalum API 비속어 검사

### 위시 리스트 API
- [ ] `POST /api/wishes` 위시 리스트 상품 추가
- [ ] `DELETE /api/wishes/{wishId}` 위시 리스트 상품 삭제
- [ ] `GET /api/wishes` 위시 리스트 상품 조회
