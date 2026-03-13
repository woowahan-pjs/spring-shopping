# 코드 리뷰 피드백

> 브랜치: `loop-study` | 대상: `Product.java`, `ProductTest.java`

---

## Product.java

### [버그] `catch (Exception e)` 가 `IllegalArgumentException` 을 삼킴

```java
// 수정 전
} catch (Exception e) {
    throw new IllegalArgumentException("상품 이미지 URL이 올바르지 않습니다.");
}

// 수정 후
} catch (URISyntaxException e) {
    throw new IllegalArgumentException("상품 이미지 URL이 올바르지 않습니다.");
}
```

- `IllegalArgumentException` 도 `Exception` 의 하위 타입이므로 http/https 검증 예외까지 잡아버림
- 의도한 에러 메시지가 아닌 catch 블록 메시지로 덮어씌워짐

---

### [설계] 부분 생성자 체이닝으로 인한 잘못된 테스트 통과

```java
Product(String name) → Product(name, BigDecimal.ZERO) → Product(name, BigDecimal.ZERO, null)
// validateImageUrl(null) 호출 → 항상 예외 발생
```

- `productName_withDisallowedSpecialChars` 테스트가 특수문자 검증이 아닌 imageUrl null 로 인해 통과
- 테스트 코드에서 전체 생성자 `Product(name, price, imageUrl)` 를 명시적으로 사용해야 함

---

### [버그] 정규식 `NAME_PATTERN` 오류

```java
// 수정 전 (버그)
"^[가-힣a-zA-Z0-9()\\[]+-&/_s]+$"

// 수정 후
"^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_\\s]+$"
```

| 문제 | 원인 |
|------|------|
| `+-&` | `-` 가 범위(range)로 해석됨. `+`(43) ~ `&`(38)는 역방향이라 `PatternSyntaxException` 발생 |
| `_s` | `\s`(공백) 가 아닌 리터럴 `_`, `s` 로 해석됨. 공백 허용 불가 |
| `\\[` 만 있고 `\\]` 없음 | `]` 문자가 허용 안 됨 |

---

### [NPE 위험] `validatePrice` null 체크 없음

```java
// 수정 전
private void validatePrice(BigDecimal price) {
    if (price.compareTo(BigDecimal.ZERO) < 0) { // price == null → NPE

// 수정 후
private void validatePrice(BigDecimal price) {
    if (price == null) {
        throw new IllegalArgumentException("상품 가격은 필수입니다.");
    }
    ...
}
```

---

### [코드품질] 매직 넘버/문자열 상수 추출

```java
// 수정 전
if (name.length() > 15) { ... }
if (price.compareTo(new BigDecimal(999999999)) > 0) { ... }

// 수정 후
private static final int MAX_NAME_LENGTH = 15;
private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(999_999_999);
private static final String NAME_PATTERN = "...";
```

---

## ProductTest.java

### [일관성] `assertDoesNotThrow` → AssertJ 스타일 통일

```java
// 수정 전 (JUnit 5)
assertDoesNotThrow(() -> new Product(specialChar, ...));

// 수정 후 (AssertJ)
assertThatCode(() -> new Product(specialChar, ...)).doesNotThrowAnyException();
```

- 프로젝트 전반에 AssertJ(`assertThatThrownBy`) 를 사용하므로 일관성 확보

---

### [일관성] `BigDecimal` 생성 방식 혼용

```java
// 혼용 중
new BigDecimal(0)   // 일부 테스트
BigDecimal.ZERO     // 다른 테스트

// BigDecimal.ZERO 또는 BigDecimal.valueOf() 로 통일
```

---

### [테스트 누락] 경계값 테스트 미흡

최대값(`999_999_999`) 성공 케이스 누락. 초과값 실패 케이스만 존재.

```java
@Test
@DisplayName("상품 가격이 최대값(999,999,999원)이면 성공한다")
void productPriceAtMaxBoundary() {
    assertThatCode(() -> new Product("피자", BigDecimal.valueOf(999_999_999), "http://a.com/a.jpg"))
            .doesNotThrowAnyException();
}
```

---

### [코드품질] 반복되는 픽스처 하드코딩

`"피자"`, `"http://a.com/a.jpg"` 가 다수의 테스트에서 중복 사용됨.

```java
private static final String VALID_NAME = "피자";
private static final String VALID_IMAGE_URL = "http://a.com/a.jpg";
```

---

## 배운 점 / 복기 포인트

1. **`catch` 범위는 최소한으로** — `Exception` 대신 구체적인 예외 타입을 잡아야 의도치 않은 삼킴을 방지한다.
2. **생성자 오버로딩 + 검증 로직 혼용 주의** — 부분 생성자가 검증을 통과하지 못하면 테스트가 엉뚱한 이유로 통과할 수 있다.
3. **정규식 문자 클래스 `[]` 안의 `-` 위치** — 리터럴 `-` 는 맨 처음/끝이나 `\\-` 로 이스케이프해야 한다. 중간에 오면 범위로 해석된다.
4. **테스트는 경계값을 양쪽 다 검증** — 최대값 성공 + 최대값 초과 실패 두 케이스 모두 작성.
5. **단일 라이브러리로 assertion 통일** — AssertJ 와 JUnit assertion 혼용 금지.

---

## 도메인 설계

### [설계] setter 대신 도메인 메서드 사용

```java
// 지양 - 의도 불명확, 검증 우회 가능
product.setName("치킨");

// 권장 - 의도 명확, 검증 포함
product.changeName("치킨");
```

도메인 메서드 안에서 검증 로직을 함께 실행하면 객체 무결성을 항상 보장할 수 있다.

---

### [설계] 예외 처리는 JPA 전환 시 붙이기 → 일부 수정

초기 계획과 달리, Controller 구현 중 `.http` 파일로 수동 테스트하면서
**존재하지 않는 상품에 update가 성공하는 버그**를 발견해 InMemory 단계에서 먼저 추가했다.

```java
// ProductService
private void validateProductExists(Long id) {
    if (productRepository.findById(id) == null) {
        throw new NoSuchElementException("존재하지 않는 상품입니다.");
    }
}
```

JPA 전환 시에는 `Optional` 반환으로 자연스럽게 교체한다.

```java
// JPA 전환 후
public Product findById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
}
```

---

### [설계] 계층별 유효성 검사 책임

| 계층 | 책임 |
|------|------|
| 도메인 | null/blank, 길이, 형식 등 불변식 |
| Repository | 저장/조회만. 유효성 검사 없음 |
| Service | 중복 체크, 외부 API 연동(비속어, 이메일 형식 등) |

---

## Member.java / MemberRepository

### [설계] MemberRepository 메서드는 요구사항 기준으로 최소화

요구사항(가입, 로그인)을 기준으로 필요한 메서드만 정의한다.

```java
// 최종 인터페이스
Member save(Member member);
Member findByEmail(String email);
```

- `findById` → 현재 요구사항에 없음. 제거
- `findAll` → 현재 요구사항에 없음. 제거 (YAGNI 원칙)
- `update`, `deleteById` → 회원 수정/삭제 기능 없음. 제거

---

### [설계] equals/hashCode 에 password 포함 금지

`password`는 동등성 기준이 아니다. 나중에 비밀번호 암호화 시 같은 회원인데 `equals`가 `false`를 반환하는 버그가 생긴다.
`email`(자연키) 또는 `id` 기반으로만 구성한다.

```java
// 권장
@Override
public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Member member = (Member) o;
    return Objects.equals(id, member.id) && Objects.equals(email, member.email);
}
```

---

## WishlistItem 설계

### [설계] Wishlist는 행(row) 방식으로 설계

`List<productId>` 를 가지는 방식 대신, 회원-상품 쌍을 하나의 행으로 표현한다.

```java
WishlistItem { id, memberId, productId }
```

| | List 방식 | 행 방식 |
|---|---|---|
| JPA 매핑 | `@ElementCollection` (복잡) | `@Entity` 하나로 끝 |
| 특정 아이템 삭제 | 리스트 조작 필요 | id로 단순 삭제 |
| 소유자 확인 | Wishlist 소유자 확인 | WishlistItem의 memberId 확인 |

- 삭제/소유자 확인 요구사항이 있으므로 행 방식이 자연스럽다.
- 삭제는 도메인이 아닌 Repository의 `deleteById`로 처리한다.

---

## InMemory Repository 공통

### [코드품질] AtomicLong 변수명 통일: `idSequence`

| 이전 | 통일 후 |
|------|---------|
| `counter` | `idSequence` |
| `idCounter` | `idSequence` |

`idSequence`는 DB의 sequence 개념과 일치하고 의도가 명확하다.

---

### [설계] InMemory save()의 동일 참조 반환 한계

InMemory 구현에서 `save()`는 전달받은 객체에 `assignId()`를 호출하고 **같은 참조를 반환**한다.
따라서 `findById` / `findByEmail` 후 `isEqualTo(saved)` 검증은 동일 참조라 항상 통과한다.

- `save` 후 `getId()` not null 확인 → ✅ id 채번 확인은 유효
- `find` 후 `isEqualTo(saved)` → ⚠️ 동일 참조라 trivial

JPA 전환 시 `save()`가 새로운 managed entity를 반환하므로 그때 비로소 `equals` 검증이 진짜 의미를 가진다.
InMemory 단계에서는 구조적 한계로 허용한다.

---

## WishlistItemTest

### [코드품질] 테스트에서 불필요한 도메인 객체 생성 금지

`WishlistItem`은 `Long` 값만 받으므로 `Member`, `Product` 객체를 굳이 만들 필요 없다.

```java
// 불필요
Member member = createMember();
member.assignId(1L);
new WishlistItem(member.getId(), product.getId())

// 충분
new WishlistItem(1L, 1L)
```

테스트는 검증 대상에 필요한 최소한의 준비만 한다.

---

## Service 계층 설계

### [설계] 외부 의존성은 인터페이스로 추상화

| 인터페이스 | 실제 구현체 | 테스트용 Fake |
|---|---|---|
| `ProfanityValidator` | `PurgoMalumValidator` (외부 API) | `FakeProfanityValidator` |
| `EmailFormatValidator` | `RegexEmailFormatValidator` (로컬 정규식) | - (실제 구현체 사용 가능) |
| `PasswordEncryptor` | `BCryptPasswordEncryptor` | `FakePasswordEncryptor` |

외부 API, 라이브러리 등 테스트하기 어려운 의존성은 인터페이스로 분리하고 테스트용 Fake를 별도로 만든다.

---

### [설계] 생성자 주입 (Constructor Injection)

```java
public ProductService(ProductRepository productRepository, ProfanityValidator profanityValidator) {
    this.productRepository = productRepository;
    this.profanityValidator = profanityValidator;
}
```

- 필드 주입(`@Autowired`)은 테스트에서 의존성 교체가 불가능하다.
- 생성자 주입은 테스트에서 `new ProductService(fakeRepo, fakeValidator)` 처럼 자유롭게 교체 가능.
- Spring 애노테이션(`@Service`, `@Component`)은 Controller 계층 연결 시 추가한다.

---

### [코드품질] 검증 로직 private 메서드 추출

`save()`와 `update()` 양쪽에서 비속어 검증이 필요한 경우, 중복 제거를 위해 private 메서드로 추출한다.

```java
private void validateProductName(Product product) {
    if (profanityValidator.containsProfanity(product.getName())) {
        throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
    }
}
```

---

## 비밀번호 암호화 (BCrypt)

### [버그] register() 이후 member.getPassword()는 해시값

```java
service.register(member);  // 내부에서 changePassword(BCrypt해시) 호출
service.login(email, member.getPassword());  // ← 해시값을 평문으로 전달 → 항상 false
```

`register()` 이후 `member` 객체의 password가 BCrypt 해시로 바뀐 상태이다.
로그인 테스트에서는 원래 평문 비밀번호를 변수로 따로 보관해야 한다.

```java
String rawPassword = "password";
Member member = new Member(email, rawPassword);
service.register(member);
service.login(email, rawPassword);  // 평문 변수 사용
```

---

### [설계] `changePassword()`는 public 필요

`assignId()`는 같은 패키지(Repository)에서만 호출 → package-private 적절.
`changePassword()`는 다른 패키지(Service)에서 호출 → **public 필요**.

---

### [설계] FakePasswordEncryptor가 필요한 이유

BCrypt는 **의도적으로 느리게 설계**된 암호화 알고리즘이다.
단위 테스트에서 BCryptPasswordEncryptor를 직접 사용하면 테스트 속도가 크게 저하된다.
FakePasswordEncryptor는 암호화 없이 평문 그대로 반환하여 테스트를 빠르게 유지한다.

---

## WishlistItemServiceTest

### [코드품질] List 크기 검증 시 AssertJ 전용 메서드 사용

```java
// 비권장
assertThat(items.size()).isEqualTo(1);
assertThat(items.size()).isEqualTo(0);

// AssertJ 스타일
assertThat(items).hasSize(1);
assertThat(items).isEmpty();
```

---

## Controller 계층

### [버그] Stream에서 `forEach` 대신 `map` 사용

```java
// 버그 - forEach는 void 반환, toList() 체이닝 불가
products.stream()
        .forEach(ProductResponse::from)
        .toList(); // 컴파일 에러

// 수정
products.stream()
        .map(ProductResponse::from)
        .toList();
```

`forEach`는 각 요소를 소비(consume)할 때, `map`은 변환할 때 사용한다.

---

### [버그] `@RequestBody` 누락

```java
// 버그 - @RequestBody 없으면 JSON 바디를 읽지 못함
public ResponseEntity<ProductResponse> createProduct(ProductRequest request) { ... }

// 수정
public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) { ... }
```

Spring은 `@RequestBody`가 있어야 HTTP 요청 바디를 역직렬화한다.

---

### [버그] Jackson 역직렬화 실패: no-arg constructor 없음

```
Cannot resolve parameter names for constructor
```

Jackson은 기본적으로 **no-arg constructor + setter** 방식으로 역직렬화한다.
DTO에 파라미터 생성자만 있고 no-arg 생성자가 없으면 예외가 발생한다.

```java
// 해결 - no-arg 생성자 추가
public ProductRequest() {}
public ProductRequest(String name, BigDecimal price, String imageUrl) { ... }
```

또는 `@JsonCreator` + `@JsonProperty` 조합으로 파라미터 생성자를 직접 지정할 수 있다.

---

### [설계] POST 응답은 201 Created

```java
// 지양 - 200 OK는 조회에 사용
return ResponseEntity.ok(ProductResponse.from(save));

// 권장 - POST(생성)는 201 Created
return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(save));
```

| HTTP 메서드 | 성공 상태 코드 |
|---|---|
| GET | 200 OK |
| POST (생성) | 201 Created |
| PUT/PATCH | 200 OK |
| DELETE | 204 No Content |

---

### [설계] `@MockBean` deprecated → `@MockitoBean` 사용

Spring Boot 3.4+에서 `@MockBean`이 deprecated되었다.
`spring-test` 모듈의 `@MockitoBean`으로 교체한다.

```java
// 이전 (deprecated)
import org.springframework.boot.test.mock.mockito.MockBean;
@MockBean ProductService service;

// 현재
import org.springframework.test.context.bean.override.mockito.MockitoBean;
@MockitoBean ProductService service;
```

---

### [코드품질] Controller 테스트 요청 바디는 도메인 객체가 아닌 DTO 사용

```java
// 비권장 - 도메인 객체를 요청 바디로 사용
content(objectMapper.writeValueAsString(product))

// 권장 - DTO를 요청 바디로 사용
ProductRequest request = new ProductRequest(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
content(objectMapper.writeValueAsString(request))
```

테스트도 실제 클라이언트가 보내는 방식과 동일한 구조를 사용해야 한다.
도메인 객체에는 Controller가 모르는 필드(예: `id`)가 포함될 수 있어 의도치 않은 직렬화가 발생할 수 있다.

---

### [코드품질] MockMvc → MockMvcTester 전환 (Spring Boot 3.4+)

Spring Framework 6.2 / Spring Boot 3.4부터 AssertJ 기반의 `MockMvcTester`를 공식 지원한다.
`@WebMvcTest`에서 `@Autowired`로 직접 주입 가능하다.

```java
// 이전 - MockMvc + Hamcrest
@Autowired MockMvc mockMvc;
@Autowired ObjectMapper objectMapper;

mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isCreated())
    .andExpect(jsonPath("$.id").isNotEmpty());

// 이후 - MockMvcTester + AssertJ
@Autowired MockMvcTester mockMvcTester;
@Autowired ObjectMapper objectMapper;  // content()는 String만 받으므로 여전히 필요

assertThat(mockMvcTester.post().uri("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
    .hasStatus(HttpStatus.CREATED)
    .bodyJson()
    .convertTo(ProductResponse.class)
    .satisfies(response -> {
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getPrice()).isEqualByComparingTo(VALID_PRICE);
    });
```

---

### [버그] MockMvcTester에 `.body()` 메서드 없음

`MockMvcTester`의 요청 빌더는 `content(String)` / `content(byte[])` 만 지원한다.
`.body(Object)` 는 존재하지 않으므로 `ObjectMapper`로 직접 직렬화해야 한다.

```java
// 존재하지 않음 - 컴파일 에러
.body(request)

// 올바른 방법
.content(objectMapper.writeValueAsString(request))
```

---

### [버그] `bodyJson().satisfies()` 람다에서 `extractingPath()` 없음

`satisfies(json -> ...)` 에서 `json`은 **assertion 객체가 아닌 실제 값(JSON 문자열)**이다.
따라서 `extractingPath()` 같은 assertion 메서드를 호출할 수 없다.

여러 경로를 체이닝으로 검증하려면 `hasPath()` 또는 `hasPathSatisfying()`을 사용한다.

```java
// 잘못된 접근
.bodyJson().satisfies(json -> {
    json.extractingPath("$.id")...  // 컴파일 에러 - json은 assertion이 아님
})

// 올바른 방법 A - 존재 여부만 확인
.bodyJson()
.hasPath("$.id")
.hasPath("$.name")

// 올바른 방법 B - 값까지 확인
.bodyJson()
.hasPathSatisfying("$.id", id -> assertThat(id).isNotNull())

// 올바른 방법 C - 타입 변환 후 검증 (권장)
.bodyJson()
.convertTo(ProductResponse.class)
.satisfies(response -> assertThat(response.getId()).isEqualTo(1L));
```

---

### [버그] JSON 역직렬화 숫자 타입 불일치 (BigDecimal vs Integer)

JSON에서 숫자를 읽을 때 Jackson은 기본적으로 다음 타입으로 파싱한다.

| JSON 값 | Jackson 기본 파싱 타입 |
|---|---|
| `0`, `1` 등 정수 | `Integer` |
| `0.5`, `1.99` 등 소수 | `Double` |
| `BigDecimal` | 기본 불가 |

`hasPathSatisfying("$.price", price -> assertThat(price).isEqualTo(VALID_PRICE))`는
`price`가 `Integer`이고 `VALID_PRICE`가 `BigDecimal`이라 타입 불일치로 실패한다.

`convertTo(ProductResponse.class)` 로 Jackson이 DTO로 역직렬화하면 `BigDecimal` 필드 타입 그대로 비교 가능하다.

---

### [버그] `convertTo()` 사용 시 no-arg constructor 필요

`bodyJson().convertTo(ProductResponse.class)` 사용 시 Jackson이 역직렬화를 시도한다.
응답 DTO에 no-arg 생성자가 없으면 `Type definition error` 예외가 발생한다.

```java
// 해결 - no-arg 생성자 추가
public ProductResponse() {}
```

---

### [설계] `ProductRequest`에 `id` 필드 추가 금지

REST API 설계에서 `id`는 URL 경로로 전달한다. 요청 바디에 `id`를 넣는 것은 잘못된 설계다.

| 요청 | id 위치 |
|---|---|
| `POST /products` | 없음 (서버가 채번) |
| `PUT /products/{id}` | URL 경로 (`/products/1`) |

`ProductRequest`에 `id`를 추가하면 직렬화 시 JSON에 포함되지만 Controller는 `@PathVariable`로 받은 값을 사용하므로 바디의 `id`는 무시된다. 불필요하고 혼란을 유발한다.

---

### [버그] `@PathVariable` 파라미터명 오류 (Spring 6.x)

```
Name for argument of type [java.lang.Long] not specified, and parameter name information
not available via reflection. Ensure that the compiler uses the '-parameters' flag.
```

Spring 6.x부터 리플렉션으로 파라미터명을 읽으려면 컴파일 시 `-parameters` 플래그가 필요하다.
어노테이션에 이름을 직접 명시하면 플래그 없이도 동작한다.

```java
// 오류 - 파라미터명 없음
@PathVariable Long id

// 해결 - 이름 명시
@PathVariable("id") Long id
```

---

---

## 패키지 구조

### [설계] 도메인별 기능형 패키지 구조로 리팩터링

레이어별 패키지(`controller/`, `service/`, `domain/`)에서 도메인별 기능형 패키지로 전환했다.

```
shopping/
├── product/
│   ├── domain/       (Product, ProductRepository, InMemoryProductRepository)
│   ├── service/      (ProductService, ProfanityValidator, PurgoMalumValidator)
│   └── controller/
│       └── dto/      (ProductRequest, ProductResponse)
├── member/
├── wishlist/
├── auth/
├── config/
└── controlleradvice/ (GlobalControllerAdvice, ErrorResponse)
```

- 도메인이 추가될수록 레이어별 구조는 파일이 분산되어 파악이 어렵다.
- 기능형 구조는 관련 파일이 한 곳에 모여 응집도가 높아진다.
- JPA 전환 시 각 도메인 패키지에 Entity, JpaRepository를 추가하는 방식으로 자연스럽게 확장된다.

---

### [코드품질] `FakePasswordEncryptor` 위치 오류

`FakePasswordEncryptor`는 `MemberService` 테스트용임에도 `product/service/` 아래에 위치했다.
`member/service/` 패키지로 이동해야 한다.

---

### [설계] DTO는 `controller/dto/` 하위로

Request/Response DTO는 HTTP 계층의 관심사이므로 `controller/` 하위에 배치한다.

```
shopping/
├── controller/
│   ├── ProductController.java
│   ├── GlobalControllerAdvice.java
│   └── dto/
│       ├── ProductRequest.java
│       ├── ProductResponse.java
│       └── ErrorResponse.java
├── service/
└── domain/
```

- `dto/`를 최상위에 두면 어느 계층에서 써야 하는지 불명확해진다.
- Service는 도메인 객체만, Controller는 DTO만 다루는 책임 분리가 패키지에서도 드러난다.
- JPA 전환 시 기능형(도메인별 패키지)으로 리팩터링 예정.

---

## .http 파일 관리

### [코드품질] .http 파일은 `http/` 폴더에서 컨트롤러별로 관리

```
http/
├── product.http
├── member.http
└── wishlist.http
```

- 프로젝트 루트에 단일 파일로 두면 컨트롤러가 늘어날수록 파일이 비대해진다.
- IntelliJ는 위치에 무관하게 `.http` 파일을 인식한다.

---

## GlobalControllerAdvice

### [버그] `NoSuchElementException` 핸들러에 404가 아닌 400 반환

```java
// 잘못됨 - 리소스 없음에 400을 반환
return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));

// 올바름 - 리소스 없음은 404
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
```

| 예외 | HTTP 상태 코드 |
|---|---|
| `IllegalArgumentException` (잘못된 입력) | 400 Bad Request |
| `NoSuchElementException` (리소스 없음) | 404 Not Found |

---

### [버그] `ErrorResponse`에 getter 없으면 JSON 직렬화 시 `{}` 반환

Jackson은 기본적으로 getter를 통해 필드를 직렬화한다.
getter가 없으면 빈 객체 `{}`가 반환되어 에러 메시지가 클라이언트에 전달되지 않는다.

```java
// 버그 - getter 없음
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message) { this.message = message; }
    // getMessage() 없음 → {} 반환
}

// 수정
public String getMessage() { return message; }
```

---

### [코드품질] `@RestControllerAdvice`는 `@WebMvcTest`에서 자동 스캔됨

`@WebMvcTest(ProductController.class)`로 특정 컨트롤러만 지정해도
`@RestControllerAdvice` 빈은 별도 설정 없이 테스트 컨텍스트에 포함된다.

---

## InMemoryProductRepository

### [버그] `update()` 에서 `assignId()` 누락

```java
// 버그 - Map key로만 id 관리, Product 객체의 id 필드는 null
public Product update(Long id, Product product) {
    productMap.put(id, product);
    return productMap.get(id);  // product.getId() == null
}

// 수정
public Product update(Long id, Product product) {
    product.assignId(id);  // ← 추가
    productMap.put(id, product);
    return productMap.get(id);
}
```

`save()`에서는 `assignId()`를 호출하고 있었으나 `update()`에서 같은 처리가 누락되었다.

---

## ProductService

### [버그] `update()` / `findById()` 에서 존재 여부 확인 없음

존재하지 않는 id로 `update()`를 호출하면 `productMap.put(id, product)`로 새 상품이 생성되는 버그.

```java
// 수정 - update(), findProductById() 앞에 존재 확인 추가
private void validateProductExists(Long id) {
    if (productRepository.findById(id) == null) {
        throw new NoSuchElementException("존재하지 않는 상품입니다.");
    }
}
```

---

### [설계] 개인정보 응답 최소화

회원가입 응답에 Member 정보를 굳이 반환할 필요 없다.
`service.register()`가 `void`라면 `201 Created` 바디 없이 반환하면 충분하다.
개인정보를 불필요하게 응답에 포함시키는 건 오버엔지니어링이다.

---

### [버그] `LoginResponse` getter 누락 → 406 Not Acceptable

Jackson은 getter를 통해 필드를 직렬화한다.
getter가 없으면 Spring이 `application/json`을 생성하지 못해 **406 Not Acceptable** 반환.

```java
// 버그 - getter 없음
public class LoginResponse {
    private String token;
    public LoginResponse(String token) { this.token = token; }
    // getToken() 없음 → 406
}

// 수정
public String getToken() { return token; }
```

---

### [버그] Controller 테스트 stub 전 mock 호출 → token null

```java
// 버그 - stub 전에 mock 메서드 호출 → null 반환
String token = provider.generate(member.getId()); // null
given(provider.generate(member.getId())).willReturn(token); // null stub

// 수정 - 리터럴로 stub 먼저
given(provider.generate(member.getId())).willReturn("mocked-token");
```

mock 객체의 메서드는 stub 이전에 호출하면 기본값(null, 0 등)을 반환한다.
항상 stub 설정을 먼저 하고 테스트 로직을 작성해야 한다.

---

### [버그] `@Autowired` 중복 (필드 + 생성자)

Spring 4.3+에서 생성자가 하나면 `@Autowired` 생략 가능.
필드에 `@Autowired`를 붙이고 생성자 주입도 사용하는 건 중복이다.

```java
// 중복
@Autowired
private final MemberService service;
public MemberController(MemberService service) { ... }

// 올바름 - 생성자 주입만 사용
private final MemberService service;
public MemberController(MemberService service) { ... }
```

---

### [버그] `InMemoryMemberRepository`, `InMemoryWishlistItemRepository` id 시작값 0

```java
// 버그 - 첫 번째 저장 id = 0
private final AtomicLong idSequence = new AtomicLong();

// 수정 - InMemoryProductRepository와 일관성
private final AtomicLong idSequence = new AtomicLong(1L);
```

`AtomicLong()` 기본값은 0이고 `getAndIncrement()`는 현재값 반환 후 증가한다.
`InMemoryProductRepository`는 `new AtomicLong(1L)`로 시작하는데 나머지가 불일치였다.

---

### [버그] `JwtTokenProvider` - `secret.getBytes()` 위험

```java
// 위험 - 짧은 문자열이면 HMAC-SHA256 키 길이(256bit) 미달로 예외 발생
this.key = Keys.hmacShaKeyFor(secret.getBytes());

// 수정 - Base64 디코딩으로 정확한 키 길이 보장
this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
```

---

### [설계] `JwtTokenProvider` 생성자 초기화 방식 (테스트 친화적)

`@PostConstruct` 방식은 Spring 컨텍스트 없이 테스트에서 직접 생성이 불가능하다.
생성자에서 바로 초기화하면 `new JwtTokenProvider(secret, expirationMs)`로 단위 테스트 가능.

```java
// @PostConstruct 방식 (테스트 불편)
@PostConstruct
private void init() {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
}

// 생성자 초기화 방식 (테스트 친화적)
public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration-ms}") long expirationMs) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expirationMs = expirationMs;
}
```

---

### [코드품질] 테스트 내 시크릿 키 중복 → 상수 추출

```java
// 중복
void setUp() { new JwtTokenProvider("dGVzdC...", 3600000L); }
void expiredToken() { new JwtTokenProvider("dGVzdC...", 1L); }

// 상수 추출
private static final String TEST_SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHk=";
```

---

### [코드품질] Controller 필드에 `final` 누락

```java
// 누락
private WishlistItemService service;

// 올바름
private final WishlistItemService service;
```

생성자 주입을 사용하는 경우 필드는 반드시 `final`로 선언해 불변성을 보장해야 한다.

---

### [설계] void 메서드 BDDMockito 스텁 패턴

`void` 메서드는 `given(service.method()).willXxx()` 패턴을 사용할 수 없다.
`void` 호출을 `given()`의 파라미터로 넣으면 컴파일 에러가 발생한다.
`willDoNothing().given()` 순서로 사용해야 한다.

```java
// 컴파일 에러 - void를 given() 파라미터로 불가
given(service.deleteById(1L)).willDoNothing();

// 올바름
willDoNothing().given(service).deleteById(1L);
```

반환값 있는 메서드와 void 메서드의 BDDMockito 패턴 혼용은 관례적으로 허용된다.

| 메서드 종류 | 패턴 |
|---|---|
| 반환값 있음 | `given(service.save(any())).willReturn(product)` |
| void | `willDoNothing().given(service).deleteById(1L)` |

---

## WishlistItemController

### [버그] `WishlistItemResponse` no-arg 생성자 누락 → JSON 역직렬화 실패

```
Type definition error: [simple type, class WishlistItemResponse]
```

`bodyJson().convertTo(WishlistItemResponse.class)` 사용 시 Jackson이 역직렬화를 시도한다.
no-arg 생성자가 없으면 `Type definition error` 예외가 발생한다.
`ProductResponse`와 동일하게 no-arg 생성자를 추가해야 한다.

---

### [설계] 위시리스트 삭제 시 소유자 검증 필요

삭제 API에서 요청자의 `memberId`와 `WishlistItem.memberId`가 일치하는지 확인해야 한다.
소유자 불일치 시 404 반환 (보안상 다른 사람의 리소스 존재 여부를 노출하지 않기 위함).

검증 위치: Service 계층 (비즈니스 규칙 = Service 책임)

---

### [버그] `@WebMvcTest`에서 `AuthInterceptor` 의존 체인으로 인한 전체 테스트 실패

`AppConfig`가 `@Configuration`이라 `@WebMvcTest` 시 자동 로드된다.
`AppConfig` → `AuthInterceptor` → `JwtTokenProvider` 체인이 존재하므로,
`JwtTokenProvider`가 웹 슬라이스에 없으면 `AuthInterceptor`를 사용하지 않는 Controller 테스트도 실패한다.

```
Unsatisfied dependency: No qualifying bean of type 'JwtTokenProvider' available
```

해결: `JwtTokenProvider`를 실제로 쓰지 않아도 빈 생성 체인을 끊기 위해 `@MockitoBean` 추가

```java
// ProductControllerTest, MemberControllerTest 모두 추가 필요
@MockitoBean
JwtTokenProvider jwtTokenProvider;
```

`AuthInterceptor`를 `AppConfig`에 등록한 시점부터 모든 `@WebMvcTest`가 영향을 받는다.

---

## JPA 전환

### [설계] 도메인 ↔ Entity 분리 (헥사고날 아키텍처)

도메인 객체와 JPA Entity를 분리하고 `infrastructure` 패키지에 인프라 관심사를 격리한다.

```
product/
├── domain/
│   ├── Product.java              ← 순수 도메인 (JPA 의존 없음)
│   └── ProductRepository.java   ← 인터페이스
└── infrastructure/
    ├── ProductEntity.java        ← JPA Entity
    ├── ProductJpaRepository.java ← JpaRepository 인터페이스
    └── ProductRepositoryImpl.java ← ProductRepository 구현체
```

의존 방향: `infrastructure → domain` (역방향 절대 금지)

---

### [설계] DB 복원용 생성자 분리

`Product.of(id, ...)` 정적 팩토리를 통해 DB 복원 시 유효성 검증을 우회한다.
검증을 통과한 데이터를 DB에서 복원할 때 재검증하면 규칙 변경 시 기존 데이터 복원 자체가 실패할 수 있다.

```java
// DB 복원 전용 private 생성자
private Product(Long id, String name, BigDecimal price, String imageUrl) {
    this.id = id; this.name = name; this.price = price; this.imageUrl = imageUrl;
}

// 정적 팩토리 - 복원 의도 명확
public static Product of(Long id, String name, BigDecimal price, String imageUrl) {
    return new Product(id, name, price, imageUrl);
}
```

---

### [설계] Entity에 getter 불필요

`@Id`가 필드에 붙으면 Hibernate가 리플렉션으로 필드에 직접 접근한다 (getter 불필요).
Entity에 getter를 열어두면 외부에서 Entity 필드를 직접 읽는 코드가 생겨 `toDomain()` 경유 원칙이 깨진다.

---

### [버그] `@GeneratedValue` strategy 누락

```java
// 누락 시 Hibernate가 별도 시퀀스 테이블 생성 시도
@GeneratedValue

// AUTO_INCREMENT 컬럼은 반드시 IDENTITY
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

---

### [버그] DB 타입 불일치 — `INT` vs `DECIMAL`

DDL의 `INT`와 `BigDecimal` 필드의 Hibernate 기대 타입 `NUMERIC(38,2)`가 불일치하면 `ddl-auto: validate` 시 기동 실패.

```sql
-- 잘못됨
price INT NOT NULL

-- 올바름 (BigDecimal → DECIMAL)
price DECIMAL(13, 2) NOT NULL
```

`DECIMAL(13, 2)` — 최대 999억, 소수점 2자리(원/전) 지원.

---

### [버그] `jakarta.transaction.Transactional` 사용 금지

Spring 프로젝트에서는 반드시 `org.springframework.transaction.annotation.Transactional`을 사용해야 한다.
`jakarta` 것은 JEE 표준으로 Spring의 `readOnly`, `propagation` 등 전용 옵션을 사용할 수 없다.

```java
// 잘못됨
import jakarta.transaction.Transactional;

// 올바름
import org.springframework.transaction.annotation.Transactional;
```

---

### [버그] `@Modifying` 후 1차 캐시 불일치

`@Modifying` JPQL UPDATE 후 JPA 1차 캐시(영속성 컨텍스트)에 기존 엔티티가 남아있어
soft delete 후 `findById()` 호출 시 삭제된 엔티티가 반환되는 버그.

```java
// 잘못됨 - 캐시 불일치 발생
@Modifying
@Query("UPDATE ProductEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")

// 올바름 - 캐시 자동 초기화
@Modifying(clearAutomatically = true)
@Query("UPDATE ProductEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
```

`@Modifying` 사용 시 `clearAutomatically = true`는 거의 항상 함께 사용해야 한다.

---

### [설계] JPA update는 더티 체킹 활용

`new Entity()`로 만든 detached 엔티티를 `save()`하면 JPA가 새 객체로 인식해 INSERT가 발생할 수 있다.
`findById()`로 가져온 managed 엔티티를 수정하면 `@Transactional` 종료 시 UPDATE가 자동 실행된다.

```java
// 잘못됨 - detached 엔티티
public void update(Product product) {
    repository.save(ProductEntity.from(product));  // INSERT 위험
}

// 올바름 - managed 엔티티 + 더티 체킹
@Transactional
public Product update(Long id, Product product) {
    ProductEntity entity = repository.findById(id).orElseThrow(...);
    entity.update(product);  // 더티 체킹 → 자동 UPDATE
    return entity.toDomain();
}
```

---

### [설계] soft delete JPQL에서 `CURRENT_TIMESTAMP` 사용

Java에서 `LocalDateTime.now()`를 파라미터로 넘기면 서버가 여러 대일 때 시간 불일치 가능.
JPQL의 `CURRENT_TIMESTAMP`는 DB 서버 시간 기준으로 통일되며 H2/MySQL 모두 호환된다.

```java
// 올바름
@Modifying(clearAutomatically = true)
@Query("UPDATE ProductEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
void deleteById(@Param("id") Long id);
```

---

### [설계] 도메인 객체에 `equals/hashCode` 추가 금지 (테스트 목적)

테스트를 위해 도메인을 오염시키지 않는다. AssertJ의 필드 단위 검증으로 대체한다.

```java
// 도메인 오염 - 금지 (테스트 목적의 equals/hashCode)

// 올바름 - AssertJ 활용
assertAll(
    () -> assertThat(found.getId()).isEqualTo(saved.getId()),
    () -> assertThat(found.getName()).isEqualTo(saved.getName())
);
```

---

### [설계] `@DataJpaTest`로 Repository 계층 테스트

JPA Repository 테스트는 `@DataJpaTest`를 사용한다. 전체 Spring 컨텍스트 없이 JPA 관련 빈만 로딩.
`@Import`로 실제 Repository 구현체를 주입한다.

```java
@DataJpaTest
@Import(ProductRepositoryImpl.class)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;
}
```

`ProductServiceTest`는 `InMemoryProductRepository`를 계속 사용한다. Service 단위 테스트에 JPA는 불필요하다.

