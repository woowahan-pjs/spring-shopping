# spring-shopping-product

## 요구사항 정리

----
```
1. 상품
```
- [x] 상품을 조회
- [x] 상품을 추가
  - [x] 이름, 가격, 이미지 URL을 입력받아 상품을 등록 
  - [x] 상품명 길이는 최대 15자까지 입력 가능
  - [x] 상품명 아래 특수 문자만 사용 가능:  `( ), [ ], +, -, &, /, _`
  - [x] 상품명에는 비속어가 포함될 수 없음([PurgoMalum](https://www.purgomalum.com/) API: 비속어 검사)
  - [x] 상품 가격은 0 보다 작을 수 없음
- [x] 상품을 수정
- [x] 상품을 삭제

```
2. 회원
```
- [x] 회원 가입
  - [x] 이메일, 비밀번호를 입력받아 회원을 생성
  - [x] 이메일 중복 및 형식 유요성 검증
- [x] 로그인
  - [x] 이메일과 비밀번호를 검증 
  - [x] 로그인 회원이 맞을 시 토큰을 발급

```
3. 위시 리스트
인증된 회원을 대상으로 위시 리스트
```
- [x] 위시 리스트에 등록된 상품 목록 조회
- [x] 위시 리스트에 상품을 추가
- [x] 위시 리스트에 있는 상품 삭제

```
4. 기타
```
- [x] 로그인 필터 기능

## API 명세

----
| Method | Path | Description | Auth |
|:---:|:---|:---|:---:|
| `POST` | `/api/members/register` | 새 회원 등록 및 JWT 발급 | X |
| `POST` | `/api/members/login` | 로그인 및 JWT 발급 | X |

### 2. 상품 API (Product)
| Method | Path | Description | Auth |
|:---:|:---|:---|:---:|
| `POST` | `/api/products` | 새 상품 등록 (Admin 전용) | O |
| `GET` | `/api/products` | 전체 상품 목록 조회 | X |
| `GET` | `/api/products/{productId}` | 상품 상세 조회 | X |
| `PUT` | `/api/products/{productId}` | 상품 정보 수정 | O |
| `DELETE` | `/api/products/{productId}` | 상품 삭제 | O |

### 3. 위시리스트 API (Wishlist)
| Method | Path | Description | Auth |
|:---:|:---|:---|:---:|
| `POST` | `/api/wishes` | 내 위시리스트에 상품 추가 | O |
| `GET` | `/api/wishes` | 내 위시리스트 목록 조회 (Fetch Join 적용) | O |
| `DELETE` | `/api/wishes/{wishId}` | 위시리스트 항목 삭제 | O |

## 구현 전략

----
```text
src/main/java/com/shop
│
├── domain
│   ├── common
│   │   └── exception: DomainException
│   ├── user
│   │   ├── Entity: User, Password
│   │   └── Repository Interface: UserRepository
│   ├── product
│   │   ├── exception: ProductNameBlankException, ProductNameInvalidCharacterException, ProductNameLengthExceededException
│   │   ├── Entity: Product, ProductName, ProfanityChecker
│   │   └── Repository Interface: ProductRepository
│   └── wishlist
│       └── Entity: WishList
│
├── application
│   ├── user
│   │   ├── Service: LoginService, RegistrationService
│   │   └── DTO: LoginRequest, TokenResponse
│   ├── product
│   │   ├── Service: ProductCommandService
│   │   └── DTO: ProductRequest
│   ├── wishlist
│   │   └── Service: WishListService
│   └── profanity
│       └── Service: ProfanityChecker
│
├── infrastructure
│   ├── persistence
│   │   ├── JpaUserRepository
│   │   └── JpaProductRepository
│   ├── security
│   │   ├── JwtTokenProvider
│   │   ├── JwtAuthenticationFilter
│   │   └── PasswordEncoder
│   └── external_api
│       ├── exception: ProfanityCheckException
│       └── PurgoMalumClient
│
└── ui
    ├── AuthController
    ├── ProductController
    └── WishlistController

```

## 개인적인 작업 목표

AI 도구를 적극 활용해보며 개발자로서 앞으로 어떤 방향성을 가져야할지 고민

- [x] 설계 및 구현 전략 의논
- [x] 개발 assist  