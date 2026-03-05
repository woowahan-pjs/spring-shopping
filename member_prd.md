# 회원 기능 구현 계획서

## 구현 순서

### 1. 도메인 & 레포지토리

- `member/domain/Member.java` - JPA 엔티티 (id, email, password)
- `member/repository/MemberRepository.java` - `Optional<Member> findByEmail(String email)`

---

### 2. DTO

- `member/dto/MemberRequest.java` - email, password 요청 바디
- `member/dto/TokenResponse.java` - token 응답

---

### 3. 서비스 (`member/service/MemberService.java`)

- `register(MemberRequest request): TokenResponse`
  1. 이메일 중복 확인 → 중복 시 400 Bad Request
  2. Member 저장
  3. JWT 발급 후 반환

- `login(MemberRequest request): TokenResponse`
  1. 이메일로 Member 조회 → 없으면 400 Bad Request
  2. 비밀번호 검증 → 불일치 시 400 Bad Request
  3. JWT 발급 후 반환

---

### 4. JWT 인증 (`auth/` 패키지)

- `auth/AuthService.java`
  - `createToken(Long memberId): String` - JWT 생성, subject에 memberId 담기
  - `getMemberId(String token): Long` - JWT 검증 후 subject 파싱
  - 토큰 만료/서명 오류 시 401 Unauthorized

> `LoginMember` 어노테이션 및 `AuthArgumentResolver`는 미구현 (위시리스트 기능 구현 시 추가 예정)

---

### 5. 컨트롤러 (`member/controller/MemberController.java`)

| 메서드 | 경로 | 응답 |
|--------|------|------|
| POST | `/api/members/register` | 201 + `{"token": "..."}` |
| POST | `/api/members/login` | 200 + `{"token": "..."}` |

---

### 6. 글로벌 예외 처리

- `global/exception/ErrorResponse.java`
- `global/exception/GlobalExceptionHandler.java`
  - `@RestControllerAdvice`로 전역 예외 처리
  - 유효성 검사 실패 → 400 Bad Request + `{"message": "..."}`
  - 인증 실패 → 401 Unauthorized + `{"message": "..."}`
  - 서버 오류 → 500 Internal Server Error + `{"message": "..."}`

---

### 7. 테스트

**MemberServiceTest.java**

| 케이스 | 기대 결과 |
|--------|-----------|
| 정상 회원 가입 | token 반환 |
| 중복 이메일 가입 | 예외 발생 |
| 정상 로그인 | token 반환 |
| 존재하지 않는 이메일 로그인 | 예외 발생 |
| 비밀번호 불일치 로그인 | 예외 발생 |
| 회원가입 시 비밀번호 암호화 저장 | 평문 != 저장값 |

**MemberControllerTest.java**

| 케이스 | 기대 결과 |
|--------|-----------|
| 회원가입 성공 | 201 + token |
| 중복 이메일 가입 | 400 + message |
| 로그인 성공 | 200 + token |
| 존재하지 않는 이메일 로그인 | 400 + message |
| 비밀번호 불일치 로그인 | 400 + message |

**AuthServiceTest.java**

| 케이스 | 기대 결과 |
|--------|-----------|
| 비밀번호 암호화 | 평문 != 암호화값 |
| 비밀번호 일치 검증 | 예외 없음 |
| 비밀번호 불일치 검증 | 예외 발생 |

---

## 주요 결정 사항

| 항목 | 내용 |
|------|------|
| 구현 언어 | Java 21 |
| JWT 라이브러리 | `jjwt 0.12.6` (build.gradle.kts에 포함됨) |
| JWT subject | `memberId` (Long → String 변환) |
| 비밀번호 저장 | Argon2id 암호화 (`Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()`) |
| 인증 실패 응답 | 401 Unauthorized |
| 유효성 실패 응답 | 400 Bad Request |
| 예외 응답 형식 | `{"message": "에러 메시지"}` |

---

## 패키지 구조

```
src/main/java/shopping/
├── Application.java
├── auth/
│   └── AuthService.java
├── config/
│   ├── JpaConfig.java
│   └── PasswordEncoderConfig.java
├── common/
│   ├── converter/
│   │   ├── EnumType.java
│   │   ├── EnumTypeConvertUtils.java
│   │   └── EnumTypeConverter.java
│   ├── entity/
│   │   └── BaseEntity.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ErrorResponse.java
│       └── UnauthorizedException.java
└── member/
    ├── api/
    │   ├── MemberController.java
    │   └── dto/
    │       ├── MemberRequest.java
    │       └── TokenResponse.java
    ├── service/
    │   └── MemberService.java
    ├── domain/
    │   └── Member.java
    └── repository/
        └── MemberRepository.java
```

---

## 구현 체크리스트

### 회원 가입
- [x] 이메일 + 비밀번호로 회원 가입
- [x] 중복 이메일 가입 시 400 반환
- [x] 비밀번호 암호화 후 저장 (Argon2id)
- [x] 가입 성공 시 JWT 토큰 발급 후 201 반환

### 로그인
- [x] 이메일 + 비밀번호로 로그인
- [x] 존재하지 않는 이메일 로그인 시 400 반환
- [x] 암호화된 비밀번호와 비교 후 불일치 시 400 반환
- [x] 로그인 성공 시 JWT 토큰 발급 후 200 반환

### JWT 인증
- [x] 회원 ID를 subject로 담아 JWT 생성
- [ ] `Authorization: Bearer {token}` 헤더에서 토큰 추출
- [ ] 토큰 검증 후 회원 식별
- [ ] 토큰 없거나 유효하지 않으면 401 반환

### Spring Security (후속 작업)
- [ ] `spring-boot-starter-security` 도입
- [ ] `SecurityFilterChain` 설정 (register/login은 permitAll, 나머지 authenticated)
- [ ] JWT 검증 필터 (`OncePerRequestFilter`) 구현
- [ ] Role 기반 인가 처리

### 예외 처리
- [x] 예외 발생 시 `{"message": "..."}` 형태로 응답
- [x] 400 / 401 / 500 상황별 HTTP 상태코드 반환

### 테스트
- [x] 정상 회원 가입 테스트
- [x] 중복 이메일 가입 테스트
- [x] 회원가입 시 비밀번호 암호화 저장 테스트
- [x] 정상 로그인 테스트
- [x] 존재하지 않는 이메일 로그인 테스트
- [x] 비밀번호 불일치 로그인 테스트

---

## 프로그래밍 제약 사항

- Google Java Style Guide 준수 (들여쓰기 4 spaces)
- 들여쓰기 최대 2단계
- 함수 최대 15줄
- 함수는 한 가지 일만 수행
- `else` 키워드 사용 금지
- `switch` 문 사용 금지
- 3항 연산자 사용 금지
- JUnit 5 + AssertJ로 기능 테스트 작성