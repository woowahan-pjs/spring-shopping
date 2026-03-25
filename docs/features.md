# 기능 구현 현황

## 필수 기능

| 구분 | 기능 | 상태 | 확인 방법 |
| --- | --- | --- | --- |
| 회원 API | `POST /api/members/register` 회원 가입 | 완료 | `AuthMemberE2eTest.Register`, `MemberControllerTest` |
| 회원 API | `POST /api/members/login` 로그인 | 완료 | `AuthMemberE2eTest.Login`, `MemberControllerTest` |
| 상품 API | `POST /api/products` 상품 생성 | 완료 | `ProductE2eTest.Create`, `ProductControllerTest` |
| 상품 API | `GET /api/products/{productId}` 상품 조회 | 완료 | `ProductE2eTest.Read`, `ProductControllerTest` |
| 상품 API | `PUT /api/products/{productId}` 상품 수정 | 완료 | `ProductE2eTest.UpdateAndDelete`, `ProductControllerTest` |
| 상품 API | `DELETE /api/products/{productId}` 상품 삭제 | 완료 | `ProductE2eTest.UpdateAndDelete`, `ProductControllerTest` |
| 상품 API | `GET /api/products` 상품 목록 조회 | 완료 | `ProductE2eTest.Read`, `ProductControllerTest` |
| 상품 검증 | 이름 최대 15자 제한 | 완료 | `ProductServiceBehaviorTest`, `ProductE2eTest.Create` |
| 상품 검증 | 허용 특수문자만 사용 | 완료 | `ProductValueObjectsTest`, `ProductE2eTest.Create` |
| 상품 검증 | 영문/한국어 비속어 검사 | 완료 | `EnglishSlangCheckerTest`, `KoreanSlangCheckerTest`, `ProductE2eTest.Create` |
| 위시 API | `POST /api/wishes` 위시리스트 상품 추가 | 완료 | `WishE2eTest.Add`, `WishControllerTest` |
| 위시 API | `DELETE /api/wishes/{wishId}` 위시리스트 상품 삭제 | 완료 | `WishE2eTest.ListAndDelete`, `WishControllerTest` |
| 위시 API | `GET /api/wishes` 위시리스트 상품 조회 | 완료 | `WishE2eTest.ListAndDelete`, `WishControllerTest` |

## 추가 기능

| 구분 | 기능 | 상태 | 확인 방법 |
| --- | --- | --- | --- |
| 인증 | `POST /api/auth/refresh` refresh token 기반 access token 재발급 | 완료 | `AuthMemberE2eTest.Refresh`, `AuthControllerTest` |
| 인증 | refresh token 회전 및 7일 로그인 유지 | 완료 | `AuthServiceTest`, `RefreshTokenManagerTest`, `AuthMemberE2eTest.Refresh` |
| 상품 검증 | 영문 비속어 외부 API 연동 | 완료 | `PurgomalumEnglishSlangAdapterTest`, `ProductE2eTest.Create` |
| 상품 검증 | 한국어 비속어 로컬 필터링 | 완료 | `KoreanSlangCheckerTest`, `ProductE2eTest.Create` |

## 미구현 항목

| 구분 | 기능 | 상태 | 확인 방법 |
| --- | --- | --- | --- |
| 회원 API | `GET /api/members/{memberId}` 회원 조회 | 미구현 | 컨트롤러 엔드포인트 없음 |
| 회원 API | `GET /api/members` 회원 목록 조회 | 미구현 | 컨트롤러 엔드포인트 없음 |
