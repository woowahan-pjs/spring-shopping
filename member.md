# Member Module

### LoginMemberService
- Removed `getPassword()` from `Member`, replaced with `Member.login()` domain method
- Added `Member.withId(Long id)` for creating copies with assigned IDs

### JWT Token Authentication
- Added `TokenProvider` port interface in module-member
- Implemented `JwtTokenProvider` in app module using JJWT (HS256, 1-hour expiration)
- Login now returns a JWT token instead of a random UUID

### BCrypt Password Hashing
- Added `PasswordEncoder` port interface in module-member
- Implemented `BcryptPasswordEncoder` in app module using Spring Security Crypto
- `RegisterMemberService` hashes passwords on registration
- `Member.login()` verifies raw password against hashed password via `PasswordEncoder`

### Dependencies Added
- `io.jsonwebtoken:jjwt-api:0.12.6` (+ impl, jackson)
- `org.springframework.security:spring-security-crypto`
