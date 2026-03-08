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
- [ ] 회원 가입
  - [ ] 이메일, 비밀번호를 입력받아 회원을 생성
  - [ ] 이메일 중복 및 형식 유요성 검증
- [ ] 로그인
  - [ ] 이메일과 비밀번호를 검증 
  - [ ] 로그인 성공 시 토큰을 발급

```
3. 위시 리스트
인증된 회원을 대상으로 위시 리스트
```
- [ ] 위시 리스트에 등록된 상품 목록 조회
- [ ] 위시 리스트에 상품을 추가
- [ ] 위시 리스트에 있는 상품 삭제

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
│   │   ├── Service: ProfanityChecker
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
│   └── wishlist
│       └── Service: WishListService
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
│       └── PurgoMalumClient
│
└── ui
├── controller
│   ├── AuthController
│   ├── ProductController
│   └── WishListController
└── exception
└── GlobalExceptionHandler
```

## 개인적인 작업 목표

AI 도구를 적극 활용해보며 개발자로서 앞으로 어떤 방향성을 가져야할지 고민

- [x] 설계 및 구현 전략 의논
- [x] 개발 assist  