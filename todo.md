# TODO: JWT 토큰 만료 시간 추가

## 현재 상태

- `JwtTokenProvider.createToken()` 에서 `.expiration()` 없이 토큰 발급
- `JwtProperties` 에 `secret` 필드만 존재
- `application.yml` 의 `jwt` 설정에 만료 시간 없음

---

## 구현 계획

### 1. `application.yml` 수정
- `jwt.expiration-ms` 항목 추가
- 기본값 예시: `3600000` (1시간 = 60 * 60 * 1000 ms)

```yaml
jwt:
  secret: spring-shopping-jwt-secret-key-must-be-long-enough
  expiration-ms: 3600000
```

---

### 2. `JwtProperties` 수정
- `expirationMs` 필드 추가 (record 컴포넌트)
- 바인딩 키: `jwt.expiration-ms`

```java
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long expirationMs) { ... }
```

---

### 3. `JwtTokenProvider.createToken()` 수정
- `.expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))` 추가
- jjwt의 `Jwts.builder()` 체인에 삽입

---

### 4. `JwtTokenProviderTest` 테스트 추가
- 만료된 토큰으로 `getMemberId()` 호출 시 `JwtException` 발생 검증
  - 테스트용으로 만료 시간을 0ms 또는 -1ms로 설정한 `JwtTokenProvider` 직접 생성하여 검증

---

## 영향 범위

| 파일 | 변경 내용 |
|------|-----------|
| `application.yml` | `jwt.expiration-ms` 추가 |
| `JwtProperties.java` | `expirationMs` 필드 추가 |
| `JwtTokenProvider.java` | `.expiration()` 추가 |
| `JwtTokenProviderTest.java` | 만료 토큰 예외 테스트 추가 |
