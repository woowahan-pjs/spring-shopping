# Q&A 정리

> 세션 중 나온 질문과 답변 복기용 정리

---

## 코드 품질 / 리팩토링

### Q. catch (Exception e) 로 잡으면 안 되나?
`IllegalArgumentException` 도 `Exception` 의 하위 타입이라 의도한 예외까지 삼켜버린다.
catch 범위는 항상 최소한으로 — 구체적인 예외 타입(e.g. `URISyntaxException`)을 사용해야 한다.

---

### Q. 정규식 문자 클래스 `[]` 안에서 `-` 를 리터럴로 쓰려면?
맨 처음 또는 맨 끝에 위치시키거나 `\\-` 로 이스케이프해야 한다.
중간에 오면 범위(range)로 해석되어 의도치 않은 동작 또는 `PatternSyntaxException` 발생.

```java
// 잘못됨 - +-& 가 범위로 해석
"[+-&]"

// 올바름
"[+\\-&]"  // 이스케이프
"[+&-]"    // 맨 끝에 위치
```

---

### Q. BigDecimal 값 비교 시 `isEqualTo()` 써도 되나?
`isEqualTo()` 는 `equals()` 를 사용하는데, `BigDecimal.equals()` 는 값과 scale을 모두 비교한다.
`new BigDecimal("15000").equals(new BigDecimal("15000.0"))` → **false**

BigDecimal 비교는 `isEqualByComparingTo()` 를 사용해야 한다. `compareTo()` 기반으로 수학적 값만 비교한다.

```java
assertThat(product.getPrice()).isEqualByComparingTo(price);
```

---

## 테스트 코드

### Q. 커밋 메시지에서 테스트 코드 개선은 `refactor` 인가?
`refactor` 는 프로덕션 코드 구조 개선에 사용한다.
테스트 코드 추가/수정/개선은 `test` 스코프를 사용한다.

```
test(Product): 테스트 픽스처 상수화 및 AssertJ 스타일 통일
```

---

### Q. `assertDoesNotThrow` 와 `assertThatCode().doesNotThrowAnyException()` 차이?
기능은 동일하지만, AssertJ 를 이미 사용하고 있다면 `assertThatCode` 로 통일하는 게 일관성 있다.
JUnit 5 assertion 과 AssertJ 를 혼용하지 않는 것이 원칙.

---

### Q. `VALID_` 접두사를 쓰는 이유가 있나?
강제된 컨벤션은 아니다. "검증을 통과하는 픽스처"임을 명시하기 위한 관례적 표현.
실패 케이스 상수(`INVALID_`)와 구분이 명확해지는 장점이 있다.
`@ValueSource` 로 실패값을 직접 넣는다면 굳이 안 써도 된다.

---

### Q. `ProductRepositoryTest` 에서 `ProductTest` 의 픽스처를 가져와서 써도 되나?
안 된다. 테스트 클래스끼리 직접 참조하면 결합도가 생긴다.
`ProductTest` 가 변경되면 `ProductRepositoryTest` 도 영향을 받는다.

---

### Q. 여러 테스트에서 공통으로 쓰이는 픽스처는 공용 클래스로 만들어도 되나?
테스트 클래스끼리 직접 참조하는 건 비권장이지만,
**픽스처 전용 클래스**를 만드는 건 괜찮다.

```java
public class ProductFixture {
    public static Product createProduct() { ... }
}
```

| 방식 | 권장 여부 |
|------|-----------|
| 테스트 클래스끼리 직접 참조 | ❌ |
| 각 테스트에 자체 픽스처 | ✅ (소규모) |
| `ProductFixture` 전용 클래스 | ✅ (중복 많을 때) |

소규모에서는 자체 픽스처로 시작하고, 중복이 많아지면 전용 클래스로 추출하는 것이 자연스럽다.

---

### Q. `save` 테스트와 `findById` 테스트가 겹치지 않나?
겹치는 게 아니라 **테스트 의도**가 다르다.

- `save` 테스트 → id가 채번됐는지 확인
- `findById` 테스트 → 저장된 데이터를 정확히 꺼내오는지 확인

`findById` 테스트에서 `save` 를 호출하는 건 **준비(arrange)** 단계일 뿐이다.

---

### Q. `save` 와 `findById` 를 하나의 테스트로 합쳐도 되나?
안 된다. 테스트는 하나의 행위만 검증해야 한다.
합치면 실패 시 `save` 문제인지 `findById` 문제인지 알 수 없다.

```java
// 분리 - 실패 원인이 명확
@Test
void save() {
    Product saved = repository.save(product);
    assertThat(saved.getId()).isNotNull();
}

@Test
void findById() {
    Product saved = repository.save(product);   // arrange
    Product found = repository.findById(saved.getId());
    assertThat(found).isEqualTo(saved);
}
```

---

## 설계 / 아키텍처

### Q. Bean Validation 라이브러리 쓰면 안 되나?
쓸 수 있지만 도메인 객체에 적용 시 트레이드오프가 있다.

| | Bean Validation | 직접 구현 |
|--|--|--|
| 코드량 | 적음 | 많음 |
| 도메인 순수성 | 인프라 애노테이션이 섞임 | 순수 Java |
| 실행 시점 | `@Valid` 트리거 필요 | 생성자에서 즉시 |

Bean Validation 은 **Controller의 Request DTO** 에 쓰고,
도메인은 생성자에서 직접 검증하는 게 일반적이다.

---

### Q. 이메일 형식 검증을 도메인에서 하지 않고 Service 에서 해도 되나?
된다. 이메일 형식 규칙(TLD 등)은 외부 기준에 따라 바뀔 수 있어 Service 에 두면 교체가 쉽다.

| 계층 | 담당 |
|------|------|
| 도메인 | null/blank 최소 불변식만 |
| Service | 이메일 형식, 중복 체크 |

비속어 검증(PurgoMalum API)을 Service 에서 처리하는 것과 같은 맥락이다.

---

### Q. 도메인 상태 변경 메서드를 `setName` 대신 `changeName` 으로 써야 하나?
`changeName` 이 더 좋다. 이유:
- 의도가 명확하게 드러남
- 메서드 내부에서 검증 로직을 함께 실행 가능
- `setter` 를 열어두면 검증을 우회할 수 있어 도메인 무결성이 깨질 수 있음

```java
public void changeName(String name) {
    validateName(name);  // 검증 포함
    this.name = name;
}
```

---

### Q. update 시 상품이 없으면 어떤 예외를 던져야 하나?
InMemory 단계에서는 예외 처리를 생략해도 된다. 항상 데이터가 있는 상황만 테스트하면 충분.
JPA 전환 시 `Optional` 반환과 함께 `NoSuchElementException` 또는 커스텀 예외를 추가하는 게 자연스럽다.

---

### Q. Repository 에서 유효성 검사를 해야 하나?
안 한다. Repository 는 저장/조회만 담당한다.
유효성은 도메인 생성자 또는 도메인 메서드(`changeName` 등)에서 이미 처리됐다고 가정한다.

| 계층 | 책임 |
|------|------|
| 도메인 | 불변식 검증 |
| Repository | 저장/조회만 |
| Service | 비즈니스 규칙 (중복, 외부 API 등) |

---

### Q. MemberRepository에 findById가 필요한가?
요구사항 기준으로 필요 없다. 회원 기능은 가입(`save`)과 로그인(`findByEmail`)뿐이다.
요구사항에 없는 메서드는 추가하지 않는다 (YAGNI 원칙).

---

### Q. MemberRepository에 findAll이 필요한가?
현재 요구사항에 회원 목록 조회가 없으므로 필요 없다.
필요한 기능이 생기면 그때 추가한다.

---

### Q. MemberFixture를 별도 클래스로 분리해야 하나?
`MemberRepositoryTest`에서만 사용한다면 테스트 내부 헬퍼 메서드로 두면 충분하다.
여러 테스트 클래스에서 공유가 필요해질 때 `MemberFixture` 클래스로 추출한다.

---

### Q. Wishlist를 List<productId>로 가지면 안 되나?
가능하지만 행(row) 방식이 더 낫다.
- 삭제 시 id로 바로 삭제 가능 (List 조작 불필요)
- JPA 전환 시 `@Entity` 하나로 자연스럽게 매핑
- 소유자 확인도 `memberId`로 단순하게 처리

---

### Q. WishlistItem 삭제는 도메인에서 처리하나?
아니다. 삭제는 Repository의 `deleteById(Long id)`로 처리한다.
도메인 객체에 삭제 로직이 필요 없다.

---

### Q. AssertJ에서 JUnit5 assertAll 같은 기능이 있나? 현업에서 쓰나?
AssertJ의 `SoftAssertions`가 대응된다.

```java
// JUnit5
assertAll(
    () -> assertThat(a).isEqualTo(1),
    () -> assertThat(b).isEqualTo(2)
);

// AssertJ
SoftAssertions.assertSoftly(softly -> {
    softly.assertThat(a).isEqualTo(1);
    softly.assertThat(b).isEqualTo(2);
});
```

공통 장점: 첫 번째 실패 후에도 나머지를 모두 실행해 실패 목록을 한 번에 확인한다.

현업에서는 자주 쓰지 않는다. 단위 테스트는 "하나의 테스트 = 하나의 검증" 원칙이 선호된다.
DTO/응답 객체 전체 상태를 한 번에 검증할 때 유용하다.

---

### Q. InMemory save()가 같은 참조를 반환하면 findBy 테스트가 의미 없는 게 아닌가?
맞다. InMemory 구현은 save()에 들어온 객체에 assignId()를 호출하고 같은 참조를 반환하기 때문에
findByEmail 후 isEqualTo(saved) 검증은 동일 참조라 항상 통과한다.

- save 후 id not null 확인은 유효 (id 채번 확인)
- findBy 후 isEqualTo는 InMemory 단계의 구조적 한계로 허용

JPA 전환 시 save()가 새로운 managed entity를 반환하므로 그때 equals 검증이 진짜 의미를 가진다.

---

## Service 계층

### Q. 로그인 실패 시 이메일 없음과 비밀번호 불일치를 구분해야 하나?
보안상 구분하지 않는 것이 일반적이다.
구분하면 공격자가 이메일 존재 여부를 탐지할 수 있다.
두 경우 모두 동일한 메시지를 반환한다.

```
"이메일 또는 비밀번호를 확인해주세요."
```

---

### Q. 로그인 결과를 Session에 저장하는 건 Service에서 하나?
아니다. Session은 웹 계층(Controller)의 관심사다.

| 계층 | 역할 |
|------|------|
| Service | 인증 처리 후 Member 반환 |
| Controller | Session/Cookie/Token에 저장 |

Service는 "이 사람이 맞는지 확인"까지만 담당한다.

---

### Q. 비밀번호 암호화를 InMemory 단계에서 도입해도 되나?
된다. BCrypt는 외부 API가 아닌 로컬 라이브러리이므로 InMemory 단계에서도 문제없다.
일찍 도입하면 실제 구조에 더 가까운 설계를 유지할 수 있다.

---

### Q. BCryptPasswordEncoder를 테스트에서 직접 써도 되나?
기능은 동작하지만 BCrypt는 의도적으로 느리게 설계되어 있어 단위 테스트 속도가 저하된다.
FakePasswordEncryptor(평문 그대로 반환)를 만들어 테스트에서 주입하는 것이 권장된다.

---

### Q. BCryptPasswordEncryptor.matches() 첫 번째 인자는 무엇인가?
첫 번째 인자는 반드시 **평문**이어야 한다. 순서를 바꾸면 항상 false가 반환된다.

```java
encoder.matches(rawPassword, encodedPassword)  // ✅ 평문, 해시 순서
encoder.matches(encodedPassword, rawPassword)  // ❌ 항상 false
```

---

### Q. WishlistService 테스트에서 ProductRepository가 왜 필요한가?
WishlistItemService가 상품 존재 여부를 확인하기 위해 ProductRepository를 주입받기 때문이다.
테스트에서 InMemoryProductRepository에 미리 상품을 저장해두어야
"존재하는 상품" 시나리오를 테스트할 수 있다.

---

### Q. 비밀번호 암호화 시 Member.changePassword()가 public이어야 하는 이유?
`assignId()`는 Repository(같은 패키지)에서 호출하므로 package-private이 맞다.
`changePassword()`는 Service(다른 패키지)에서 호출하므로 public이 필요하다.

| 메서드 | 호출자 패키지 | 접근제어 |
|--------|-------------|----------|
| `assignId()` | `shopping.domain` (Repository) | package-private |
| `changePassword()` | `shopping.service` (Service) | public |

---

## Controller 계층 / MockMvc 테스트

### Q. `@WebMvcTest`란 무엇인가?
Spring의 슬라이스 테스트 중 하나로, **웹 계층만** 로딩한다.
`DispatcherServlet`, `Filter`, `Controller` 등 MVC 관련 빈만 생성하고
`Service`, `Repository` 등 나머지 빈은 로딩하지 않는다.
Service 등 의존성은 `@MockitoBean`으로 대체해서 제공한다.

---

### Q. `@InjectMocks`와 `@MockitoBean`의 차이는?

| | `@InjectMocks` | `@MockitoBean` |
|---|---|---|
| Spring 컨텍스트 | 없음 (순수 Mockito) | `@WebMvcTest`와 함께 Spring 컨텍스트 사용 |
| 사용 위치 | 단위 테스트 | 슬라이스 테스트 |
| MockMvc 사용 | 불가 (직접 주입 필요) | 가능 (`@Autowired MockMvc`) |
| 속도 | 빠름 | 상대적으로 느림 |

Controller 테스트에서 HTTP 요청/응답 흐름(직렬화, 상태코드 등)을 검증하려면 `@WebMvcTest` + `@MockitoBean`이 적합하다.

---

### Q. Controller 테스트에서 `ObjectMapper`를 왜 선언하는가?
`MockMvc`의 `content()`에는 `String`을 전달해야 한다.
`ObjectMapper.writeValueAsString(object)`를 사용해 Java 객체를 JSON 문자열로 변환하기 위해 필요하다.
`@WebMvcTest` 환경에서는 Spring이 `ObjectMapper`를 자동으로 빈으로 등록하므로 `@Autowired`로 주입 가능하다.

```java
content(objectMapper.writeValueAsString(request))
```

---

### Q. REST URL에서 단수(`/product`) vs 복수(`/products`) 어느 게 맞나?
복수형 `/products`가 REST 컨벤션이다.
URL은 **컬렉션 리소스**를 나타내고, `GET /products`는 "상품 목록", `POST /products`는 "상품 컬렉션에 추가" 의미이다.
단수형을 쓰면 컬렉션과 단건 리소스를 구분하기 어렵고 팀 간 혼선이 생긴다.

| URL | 의미 |
|---|---|
| `GET /products` | 상품 목록 조회 |
| `POST /products` | 상품 생성 |
| `GET /products/{id}` | 단건 상품 조회 |
| `PUT /products/{id}` | 상품 수정 |
| `DELETE /products/{id}` | 상품 삭제 |

---

### Q. MockMvcTester란 무엇이고 MockMvc와 어떻게 다른가?

Spring Framework 6.2 / Spring Boot 3.4+에서 도입된 AssertJ 기반 MockMvc 래퍼다.

| | MockMvc | MockMvcTester |
|---|---|---|
| Assertion 방식 | Hamcrest (`jsonPath`, `status`) | AssertJ (Fluent API) |
| 예외 처리 | `throws Exception` 필요 | 내부 처리, 불필요 |
| JSON → 객체 변환 | 불편 | `convertTo()`로 바로 변환 |
| Spring Boot 지원 | 전 버전 | 3.4+ |

`@WebMvcTest`에서 `@Autowired MockMvcTester`로 직접 주입 가능하다.

---

### Q. MockMvcTester에서 요청 바디를 어떻게 설정하나?

`.content(String)` 또는 `.content(byte[])`만 지원한다. `.body(Object)` 메서드는 없다.
`ObjectMapper`로 직접 직렬화해서 전달해야 한다.

```java
.contentType(MediaType.APPLICATION_JSON)
.content(objectMapper.writeValueAsString(request))
```

---

### Q. `bodyJson().satisfies()` 람다에서 `extractingPath()`를 찾을 수 없는 이유?

`satisfies(consumer)`의 파라미터는 **assertion 객체가 아닌 실제 값**이다.
따라서 `extractingPath()` 같은 assertion 메서드가 없다.

여러 필드를 검증하는 올바른 방법:

```java
// 존재 여부만 확인 (체이닝 가능)
.bodyJson().hasPath("$.id").hasPath("$.name")

// 값 확인 (체이닝 가능)
.bodyJson().hasPathSatisfying("$.price", p -> assertThat(p).isNotNull())

// DTO로 변환 후 검증 (권장)
.bodyJson().convertTo(ProductResponse.class)
    .satisfies(response -> assertThat(response.getId()).isEqualTo(1L));
```

---

### Q. `hasPathSatisfying`에서 BigDecimal 비교 시 타입 오류가 나는 이유?

JSON 숫자 `0`은 Jackson이 `Integer`로 파싱한다. `VALID_PRICE`는 `BigDecimal`이라 타입 불일치 실패한다.
`convertTo(ProductResponse.class)`로 DTO 역직렬화하면 필드 타입 그대로 `BigDecimal` 비교 가능하다.

---

### Q. `PUT /products/{id}` 요청 시 `ProductRequest`에 `id` 필드가 필요한가?

필요 없다. REST API에서 `id`는 URL 경로(`@PathVariable`)로 전달한다.
요청 바디에 `id`를 추가하면 JSON에 포함되지만 Controller는 경로의 값을 사용하므로 무시된다.
불필요한 필드는 혼란을 유발한다.

---

### Q. `@PathVariable Long id`에서 파라미터명 오류가 나는 이유?

Spring 6.x부터 파라미터명을 리플렉션으로 읽으려면 컴파일 시 `-parameters` 플래그가 필요하다.
없으면 `Name for argument not specified` 예외가 발생한다.
어노테이션에 이름을 명시하면 해결된다: `@PathVariable("id") Long id`.

---

### Q. void 메서드를 BDDMockito로 스텁하는 방법?

`void` 메서드는 `given(service.method())` 패턴이 불가능하다 (컴파일 에러).
`willDoNothing().given(service).method(args)` 순서로 사용한다.

```java
// 컴파일 에러
given(service.deleteById(1L)).willDoNothing();

// 올바름
willDoNothing().given(service).deleteById(1L);
```

---

### Q. `given().willReturn()`과 `willDoNothing().given()` 패턴을 혼용해도 되나?

된다. 두 패턴은 역할이 다르므로 혼용이 자연스럽다.

| 메서드 종류 | 패턴 |
|---|---|
| 반환값 있음 | `given(service.save(any())).willReturn(product)` |
| void | `willDoNothing().given(service).deleteById(1L)` |

---

### Q. DTO 패키지는 어디에 두어야 하나?

Request/Response DTO는 HTTP 계층의 관심사이므로 `controller/dto/` 하위가 적합하다.

```
controller/
├── ProductController.java
└── dto/
    ├── ProductRequest.java
    ├── ProductResponse.java
    └── ErrorResponse.java
```

최상위 `dto/`는 어느 계층에서도 쓸 수 있는 것처럼 보여 책임이 불분명해진다.
Service는 도메인 객체만, Controller는 DTO만 다룬다는 원칙을 패키지 구조에서도 드러낸다.

---

### Q. `.http` 파일은 어디에 두는 게 좋나?

프로젝트 루트에 `http/` 폴더를 만들고 컨트롤러별로 분리한다.

```
http/
├── product.http
├── member.http
└── wishlist.http
```

IntelliJ는 위치와 무관하게 `.http` 파일을 인식하므로 폴더 구성에 제약이 없다.

---

### Q. `@RestControllerAdvice`는 `@WebMvcTest`에서 자동으로 포함되나?

된다. `@WebMvcTest(ProductController.class)`로 특정 컨트롤러를 지정해도
`@ControllerAdvice` / `@RestControllerAdvice` 빈은 자동으로 스캔된다.
별도 설정 없이 예외 핸들러 테스트가 동작한다.

---

### Q. Jackson 직렬화 시 getter가 없으면 어떻게 되나?

Jackson은 기본적으로 getter를 통해 필드를 직렬화한다.
getter가 없는 private 필드는 직렬화되지 않아 JSON에 해당 필드가 포함되지 않는다.

```java
// getter 없음 → {} 반환
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message) { this.message = message; }
}

// getter 추가 → {"message": "..."} 반환
public String getMessage() { return message; }
```

---

### Q. `NoSuchElementException`은 400과 404 중 어느 상태 코드가 맞나?

404 Not Found가 맞다.

| 상황 | 상태 코드 |
|---|---|
| 잘못된 입력값 (`IllegalArgumentException`) | 400 Bad Request |
| 리소스가 없음 (`NoSuchElementException`) | 404 Not Found |

400은 "요청 자체가 잘못됨", 404는 "요청은 올바르지만 리소스가 없음"이다.

---

### Q. `@MockBean`이 deprecated된 이유는?
Spring Boot 3.4에서 `@MockBean` / `@SpyBean`이 `spring-boot-test`에서 deprecated되었다.
Spring Framework 자체에서 `@MockitoBean` / `@MockitoSpyBean`을 `spring-test` 모듈에 공식 추가했기 때문이다.
기능은 동일하므로 import 경로만 변경하면 된다.

```java
// 이전
import org.springframework.boot.test.mock.mockito.MockBean;

// 현재
import org.springframework.test.context.bean.override.mockito.MockitoBean;
```

---

### Q. `@WebMvcTest`에서 `@Component` 빈(AuthInterceptor)이 자동으로 로딩되지 않는 이유?

`@WebMvcTest`는 슬라이스 테스트로 `@Controller`, `@ControllerAdvice` 등 웹 계층 빈만 로딩한다.
`@Component`로 등록된 `AuthInterceptor`는 기본적으로 로딩되지 않는다.
`WebMvcConfigurer`(`AppConfig`)도 마찬가지로 로딩되지 않아 인터셉터가 등록되지 않는다.

인터셉터 동작(401 응답 등)을 테스트하려면 `@Import`로 명시적으로 로딩해야 한다.

```java
@WebMvcTest(WishlistItemController.class)
@Import(AuthInterceptor.class)
class WishlistItemControllerTest {
    @MockitoBean JwtTokenProvider tokenProvider;
    // AuthInterceptor는 real 빈 사용, JwtTokenProvider는 mock
}
```

---

### Q. Interceptor에서 `request.setAttribute()`로 설정한 값을 Controller에서 받는 방법?

`@RequestBody`는 HTTP 요청 바디를 역직렬화하는 어노테이션이다. Interceptor가 `setAttribute()`로 설정한 값은 요청 바디가 아닌 서블릿 요청 속성(attribute)이다.
`@RequestAttribute`를 사용해야 한다.

```java
// AuthInterceptor
request.setAttribute("memberId", memberId);

// Controller
@PostMapping("/wishlist")
public ResponseEntity<Void> create(
        @RequestAttribute("memberId") Long memberId,  // ✅
        @RequestBody WishlistItemRequest request) { ... }
```

---

### Q. Controller 테스트에서 stub 설정 전에 mock을 호출하면 어떻게 되나?

Mockito의 mock은 stub이 없으면 기본값을 반환한다 (객체 → null, 기본형 → 0 등).
stub 설정 전에 mock을 호출한 결과를 변수에 담으면 항상 null이 들어간다.

```java
// 잘못됨 - stub 전에 호출 → token == null
Member member = new Member(1L, "test@test.com", "password");
String token = provider.generate(member.getId());  // null (stub 미설정)
given(provider.generate(member.getId())).willReturn(token);  // null 스텁

// 올바름 - 리터럴 값 사용
String token = "mock-token";
given(provider.generate(any())).willReturn(token);
```

---

### Q. JwtTokenProvider를 `@PostConstruct` 대신 생성자에서 초기화하는 이유?

생성자에서 초기화하면 Spring 컨텍스트 없이 직접 객체를 생성할 수 있어 단위 테스트가 쉬워진다.

```java
// @PostConstruct 방식 - Spring 없이 테스트 불가
@PostConstruct
private void init() { this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); }

// 생성자 방식 - 직접 인스턴스 생성 가능
public JwtTokenProvider(@Value("${jwt.secret}") String secret, ...) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
}

// 테스트에서 직접 생성 가능
JwtTokenProvider provider = new JwtTokenProvider(secret, 3600000L);
```

---

### Q. JJWT 0.11.x → 0.12.x에서 바뀐 API는?

| 0.11.x (deprecated) | 0.12.x |
|---|---|
| `.setSubject(str)` | `.subject(str)` |
| `.setIssuedAt(date)` | `.issuedAt(date)` |
| `.setExpiration(date)` | `.expiration(date)` |
| `signWith(key, SignatureAlgorithm.HS256)` | `signWith(key)` (알고리즘 자동 추론) |
| `Jwts.parserBuilder()` | `Jwts.parser()` |
| `.setSigningKey(key)` | `.verifyWith(key)` |
| `.parseClaimsJws(token)` | `.parseSignedClaims(token)` |
| `.getBody()` | `.getPayload()` |

---

### Q. `ExpiredJwtException`을 `JwtException`보다 먼저 catch해야 하는 이유?

`ExpiredJwtException`은 `JwtException`의 하위 타입이다.
catch 블록은 위에서 아래로 순서대로 매칭되므로, `JwtException`을 먼저 두면 `ExpiredJwtException`도 잡혀 구체적인 만료 메시지를 줄 수 없다.

```java
// 올바름 - 구체적 예외를 먼저
try { ... }
catch (ExpiredJwtException e) { throw new IllegalArgumentException("만료된 토큰입니다."); }
catch (JwtException e) { throw new IllegalArgumentException("유효하지 않은 토큰입니다."); }

// 잘못됨 - ExpiredJwtException도 JwtException으로 잡힘
catch (JwtException e) { ... }
catch (ExpiredJwtException e) { ... }  // 절대 실행되지 않음
```

---

## 인증 / JWT

### Q. Session 방식과 JWT 방식의 차이는?

| | Session | JWT |
|---|---|---|
| 상태 | 서버에 저장 (Stateful) | 토큰 자체에 정보 (Stateless) |
| 전송 | 브라우저가 Cookie로 자동 전송 | JS가 Authorization 헤더에 직접 전송 |
| 서버 부담 | 세션 저장소 필요 | 검증만 수행 |
| 확장성 | 서버 간 세션 공유 필요 | 토큰만 검증하면 됨 |

---

### Q. JWT 토큰을 응답 헤더가 아닌 바디로 전달하는 이유?

두 방식 모두 사용된다. 이 프로젝트에서는 바디로 전달한다.

```json
{"token": "eyJhbGciOiJIUzI1NiJ9..."}
```

JS에서 바디를 파싱해 `localStorage`에 저장하고, 이후 요청마다 `Authorization: Bearer {token}` 헤더에 직접 담아 전송한다.
헤더 전달 방식은 서버가 직접 헤더를 설정하며, 클라이언트가 바디 파싱 없이 바로 다음 요청에 사용할 수 있다.

---

### Q. HMAC-SHA256에서 `secret.getBytes()` 대신 `Decoders.BASE64.decode()` 를 써야 하는 이유?

HMAC-SHA256은 최소 256비트(32바이트) 키를 요구한다.
`getBytes()`는 문자열을 그대로 바이트 배열로 변환하므로, 문자열이 짧으면 키 길이 부족 예외가 발생한다.
Base64로 인코딩된 시크릿을 디코딩하면 원본 바이트 배열을 복원하므로 키 길이를 보장할 수 있다.

```java
// 위험 - 문자열 길이에 따라 키 길이 미달 가능
Keys.hmacShaKeyFor(secret.getBytes())

// 안전 - Base64 디코딩으로 충분한 길이 보장
Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
```

---

### Q. JWT 시크릿 키를 Git에 올리지 않으려면?

`application.properties`에는 환경변수 플레이스홀더만 남기고, 실제 값은 환경변수 또는 `.gitignore`된 파일에 둔다.

```properties
# application.properties (커밋됨)
jwt.secret=${JWT_SECRET}
jwt.expiration-ms=3600000

# src/test/resources/application.properties (커밋됨, 테스트 전용 키)
jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHk=
```

실제 운영 값은 CI/CD 환경변수 또는 `application-local.properties`(`.gitignore` 추가)에 설정한다.

---

### Q. `AtomicLong.getAndIncrement()`의 초기값과 동작 방식은?

`new AtomicLong()` → 초기값 0. `getAndIncrement()`는 현재 값을 반환한 후 증가시킨다.
따라서 첫 번째 `save()` 호출 시 id가 0이 된다.

`new AtomicLong(1L)` → 초기값 1. 첫 번째 id가 1이 된다.

```java
// id 1부터 시작하려면
private final AtomicLong idSequence = new AtomicLong(1L);
```

`InMemoryProductRepository`는 `1L`로 시작하지만 `InMemoryMemberRepository`는 기본값 `0`으로 시작하는 불일치가 있다.

---

### Q. WishlistItem과 Wishlist 중 어떤 이름이 더 적합한가?

`WishlistItem`이 더 적합하다.

- `WishlistItem` = 위시리스트에 담긴 개별 상품 하나를 의미
- `Wishlist`로 이름을 바꾸면 컬렉션인지 단일 항목인지 의미가 모호해짐
- JPA 전환 시 `Wishlist`(컬렉션 엔티티) ↔ `WishlistItem`(자식 엔티티) 부모-자식 관계로 확장 가능성이 있으므로 `WishlistItem`을 유지하는 것이 자연스럽다.

---

### Q. `@WebMvcTest`에서 `AuthInterceptor`를 쓰지 않는 Controller도 `JwtTokenProvider` Mock이 필요한 이유?

`AppConfig`(`WebMvcConfigurer` 구현체)가 `@Configuration`이라 `@WebMvcTest` 로딩 시 함께 로드된다.
`AppConfig` → `AuthInterceptor` → `JwtTokenProvider` 의존 체인 때문에, 실제로 JWT를 사용하지 않는 Controller 테스트도 `JwtTokenProvider` 빈이 없으면 실패한다.

`@MockitoBean JwtTokenProvider`는 "실제로 쓰기 위해서"가 아닌 **빈 생성 체인을 끊기 위해** 추가하는 것이다.

---

### Q. SI/SM 시절 컨트롤러부터 레포지토리까지 모든 곳에 로그를 찍는 건 좋은 패턴인가?

안 좋은 패턴이다. 문제점:

1. **노이즈 과다** — 정상 흐름의 모든 진입/완료 로그가 쌓여 정작 중요한 장애 로그를 찾기 어렵다.
2. **성능 저하** — 로그 I/O 비용이 트래픽에 비례해 증가한다.
3. **민감 정보 노출 위험** — `log.info("로그인: email={}, password={}")` 같은 코드가 실제로 자주 발견된다.

올바른 기준: **"이 로그가 없으면 장애 원인을 찾을 수 없는가?"** 로 판단한다.

| 로그가 필요한 곳 | 필요 없는 곳 |
|---|---|
| 인증 실패/성공 | 단순 CRUD 진입/완료 |
| 외부 API 호출 | 정상 흐름 모든 단계 |
| 예외 처리 지점 | Repository 단순 조회 |

---

### Q. 현업에서 로깅 프레임워크는 주로 무엇을 쓰나?

Spring Boot 기본값인 **SLF4J + Logback**이 가장 많이 쓰인다.
Log4j2는 Logback보다 성능이 좋지만(특히 비동기 로깅) 설정이 복잡하고, log4j 1.x 보안 취약점(Log4Shell) 이슈로 일부 기피 심리가 존재한다(2.x는 무관).

스터디/일반 프로젝트에서는 Logback 기본값을 그대로 사용하고 `@Slf4j`만 추가하는 방식이 실용적이다.

---

### Q. `@Slf4j`는 어떤 계층에 붙이는 게 적합한가?

| 클래스 | 로그 내용 | 레벨 |
|--------|-----------|------|
| `AuthInterceptor` | 인증 성공/실패 | info/warn |
| `GlobalControllerAdvice` | 예외 발생 | warn/error |
| `*Service` | 주요 비즈니스 흐름 | info/warn |

Controller는 인터셉터와 어드바이스에서 커버되므로 로그를 직접 찍는 경우가 많지 않다.

로그 레벨 기준:
- `log.info()` → 정상 흐름
- `log.warn()` → 비즈니스 예외 (존재하지 않는 리소스, 소유자 불일치 등)
- `log.error()` → 예상치 못한 예외

---

### Q. 패키지를 레이어별로 구성하는 것과 도메인별로 구성하는 것의 차이는?

| | 레이어별 | 도메인별(기능형) |
|---|---|---|
| 구조 | `controller/`, `service/`, `domain/` | `product/`, `member/`, `wishlist/` |
| 도메인 추가 시 | 각 레이어 폴더에 파일 분산 | 한 폴더에 응집 |
| 파악 용이성 | 계층 흐름 파악에 유리 | 기능 단위 파악에 유리 |
| 멀티 모듈 전환 | 어렵 | 자연스럽게 모듈로 추출 가능 |

이 프로젝트는 JPA 전환 시 도메인별 패키지로 리팩터링 후 Entity, JpaRepository를 각 도메인 패키지에 추가하는 방향으로 진행한다.

---

### Q. 위시리스트 삭제 시 소유자 불일치를 어떤 예외로 처리해야 하나?

보안상 **404 Not Found** 반환이 일반적이다.
403을 반환하면 해당 리소스가 존재한다는 정보를 노출하게 된다.
404를 반환하면 "존재하지 않는 것처럼" 처리하여 다른 사람의 리소스 존재 여부를 숨길 수 있다.

---

### Q. 회원가입 성공 시 회원 정보를 응답으로 반환해야 하는가?

보안상 최소한의 정보만 반환하는 것이 원칙이다.
회원가입 완료 자체가 성공 신호이므로 `201 Created` + 빈 바디만으로 충분하다.
이름, 이메일 등 개인정보를 응답에 포함하면 불필요한 데이터 노출이 된다.

```java
@PostMapping("/members")
public ResponseEntity<Void> register(@RequestBody MemberRequest request) {
    service.register(request.toMember());
    return ResponseEntity.status(HttpStatus.CREATED).build();  // 빈 바디
}
```

---

## JPA 전환

### Q. Flyway란 무엇인가?

DB 스키마를 코드처럼 버전 관리하는 도구다. 마이그레이션 파일을 순서대로 실행하여 어떤 서버, 어떤 개발자 로컬이든 동일한 스키마를 보장한다.

- 앱 기동 시 `flyway_schema_history` 테이블 확인 → 미적용 파일 순서대로 실행
- 한 번 적용된 파일은 절대 수정 불가 (체크섬 불일치 시 기동 실패)
- 수정이 필요하면 새 버전 파일(`V2__xxx.sql`) 생성

`ddl-auto: validate`와 함께 사용한다. `create`/`update`는 운영에서 절대 금지.

---

### Q. `created_at`, `updated_at`, `deleted_at`은 모든 테이블에 넣어야 하나?

현업 과제테스트 기준으로 모든 테이블에 넣는 것이 권장된다.

- `created_at`, `updated_at` — 장애 추적, 감사(audit), 정산 등에 필수
- `deleted_at` (soft delete) — 커머스/금융에서는 실제 삭제 대신 표준
- `DATETIME(6)` 사용 — 마이크로초 정밀도, 동시성 높은 환경에서 순서 보장

---

### Q. `updated_by` 컬럼을 product 테이블에 추가해야 하나?

추가하지 않는다. "누가 언제 뭘 바꿨나"는 `product_history` 테이블의 관심사다.
`updated_by` 하나로는 어떤 필드를, 뭐에서 뭐로, 왜 바꿨는지 알 수 없다.
히스토리 테이블 없이 `updated_by`만 달랑 넣으면 설계를 이해 못 한다는 인상을 준다.

---

### Q. 도메인 객체와 JPA Entity를 왜 분리하나?

도메인 객체가 `@Column`, `@SQLRestriction` 같은 인프라 관심사로 오염되고,
JPA 제약(`var`, 기본 생성자, `open class`)으로 인해 도메인 설계가 왜곡되기 때문이다.
분리하면 도메인은 JPA도 Spring도 모르는 순수 비즈니스 규칙 객체가 된다.

의존 방향: `infrastructure → domain` (역방향 절대 금지)

---

### Q. JPA Entity에 getter가 필요한가?

`@Id`를 필드에 붙이면 Hibernate가 리플렉션으로 직접 접근하므로 getter가 불필요하다.
오히려 getter를 열어두면 외부에서 Entity를 도메인처럼 사용하는 나쁜 습관이 생긴다.
`toDomain()`으로만 접근하도록 강제하는 것이 더 좋은 설계다.

---

### Q. JPA 영속성 컨텍스트(1차 캐시)란 무엇인가?

JPA가 관리하는 엔티티 캐시다. `findById()`로 가져온 엔티티는 영속성 컨텍스트에 등록(managed)된다.

- `findById()` → managed → 더티 체킹 동작 → `@Transactional` 종료 시 자동 UPDATE
- `new Entity()` → detached → 더티 체킹 없음 → `save()` 호출 시 불안정

`@Modifying` JPQL 실행 후에는 `clearAutomatically = true`로 캐시를 비워야 한다. 안 비우면 soft delete 후 `findById()` 호출 시 삭제된 엔티티가 반환된다.

---

### Q. `@SQLRestriction`이란 무엇인가?

Hibernate 6에서 추가된 애노테이션. Entity에 항상 적용되는 WHERE 조건을 붙인다.

```java
@SQLRestriction("deleted_at IS NULL")
public class ProductEntity { ... }
```

모든 SELECT 쿼리에 `AND deleted_at IS NULL` 자동 추가. soft delete 구현 시 필수.
이전 버전(`@Where`)은 Hibernate 6에서 deprecated.

soft delete UPDATE는 `@Modifying(clearAutomatically = true)` JPQL로 직접 작성해야 한다.
(`@SQLRestriction` 때문에 `findById()` 후 `save()`로 update 불가)

---

### Q. `@DataJpaTest`와 `@SpringBootTest`의 차이는?

| | `@DataJpaTest` | `@SpringBootTest` |
|---|---|---|
| 로딩 범위 | JPA 관련 빈만 | 전체 Spring 컨텍스트 |
| 속도 | 빠름 | 느림 |
| 사용 대상 | Repository 테스트 | 통합 테스트 |
| DB | 기본 H2 인메모리 | 설정 따라 다름 |

Repository 테스트는 `@DataJpaTest`를 사용하고, `@Import`로 실제 구현체를 주입한다.

---

### Q. H2 콘솔에서 인메모리 DB에 접속하는 방법은?

`application.yaml`의 datasource URL과 동일하게 입력해야 한다.
H2 콘솔 로그인 화면의 JDBC URL은 자동으로 연동되지 않는다.

```
JDBC URL: jdbc:h2:mem:shopping
Username: sa
Password: (빈칸)
```

`DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` 옵션이 없으면 마지막 연결 끊김 시 DB가 사라진다.

---

### Q. DB 컬럼 DATETIME을 Java에서 어떤 타입으로 선언해야 하나?

`LocalDateTime`을 사용한다. `java.util.Date`는 불변이 아니고 시간대 처리가 엉망이라 Java 8 이후 deprecated 수준이다.

```java
import java.time.LocalDateTime;

private LocalDateTime createdAt = LocalDateTime.now();
private LocalDateTime deletedAt;  // nullable → null 기본값
```

| DB 타입 | Java/Kotlin 타입 |
|---------|-----------------|
| `DATETIME(6)` | `LocalDateTime` ✅ |
| `DATETIME(6)` | `java.util.Date` ❌ |
| `DATE` | `LocalDate` |

---

### Q. 공통 컬럼(created_at, updated_at, deleted_at)을 추상 클래스로 분리해도 되나?

된다. JPA는 `@MappedSuperclass`를 이 목적으로 제공한다.

```java
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime deletedAt;
}
```

- `@MappedSuperclass` — 부모 테이블 없음, 공통 컬럼만 상속
- `@Inheritance` — 부모 테이블 생성 (상속 관계 표현 용도)

네이버/카카오/토스에서 `BaseEntity` 없는 프로젝트는 거의 없다.

---

### Q. `@DataJpaTest`에서 도메인 인터페이스를 `@Import`하면 어떻게 되나?

기동 실패한다. 도메인 인터페이스는 순수 Java 인터페이스이므로 Spring이 빈으로 등록할 수 없다.

```java
// 잘못됨 - 기동 실패
@Import(MemberRepository.class)

// 올바름 - 구현체 Import, 인터페이스로 주입
@Import(MemberRepositoryImpl.class)
...
@Autowired
private MemberRepository repository;  // 인터페이스로 주입
```

이것이 DI의 핵심이다. 인터페이스로 받고, 구현체가 주입된다.

---

### Q. `@Query`가 불필요한 경우는?

Spring Data JPA는 메서드명만으로 쿼리를 자동 생성한다. 단순 조건 조회는 `@Query` 없이도 된다.

```java
// @Query 불필요
Optional<MemberEntity> findByEmail(String email);
Optional<MemberEntity> findByEmailAndDeletedAtIsNull(String email);
List<MemberEntity> findByMemberId(Long memberId);
```

`@Query`는 복잡한 조인, 서브쿼리, 네이티브 쿼리에서만 사용한다.
단순 조회에 `@Query`를 쓰면 불필요한 유지보수 포인트만 늘어난다.

---

### Q. 서비스 계층 테스트를 JPA 전환 후에도 InMemory로 유지하는 이유?

Service는 Repository 인터페이스에만 의존하기 때문이다.

```
ProductService → ProductRepository (interface)
                    ↙              ↘
   InMemoryProductRepository    ProductRepositoryImpl
   (테스트용)                    (운영용)
```

Service 테스트에 `@DataJpaTest`나 `@SpringBootTest`를 쓰면:
- Spring 컨텍스트 로딩 → 느림
- DB 연결 필요 → 환경 의존성 생김
- 비즈니스 로직 테스트에 인프라가 끼어듦

Service 테스트는 순수 Java 단위 테스트여야 한다.

---

### Q. 회원 탈퇴 기능이 없으면 `MemberEntity`에 `@SQLRestriction`이 필요한가?

불필요하다. YAGNI(You Ain't Gonna Need It) 원칙.

`deleted_at` 컬럼은 미래 확장을 위해 DDL에만 존재한다.
실제 soft delete 기능이 생길 때 `@SQLRestriction`을 추가하면 된다.
없는 기능을 위한 제약을 미리 추가하면 코드 복잡도만 올라간다.

---

### Q. 현업에서 FK(Foreign Key)를 실제로 사용하나?

네이버/카카오/토스 수준의 대규모 서비스에서는 대부분 FK를 사용하지 않는다.

이유:
1. **성능** — INSERT/UPDATE/DELETE마다 FK 체크 쿼리 추가 발생
2. **샤딩** — DB를 분산할 때 FK가 걸림돌
3. **마이크로서비스** — 서비스별 DB가 분리되면 FK 자체가 불가능
4. **배포 복잡도** — 테이블 간 의존성으로 마이그레이션 순서 강제

대신 애플리케이션 레벨에서 정합성을 보장한다 (Service에서 존재 여부 확인 등).

---

### Q. `JpaRepository` 메서드에서 도메인 객체를 반환 타입으로 써도 되나?

안 된다. `JpaRepository`는 반드시 Entity를 반환해야 한다.

```java
// 잘못됨 - Spring Data JPA가 타입을 맞출 수 없음
List<Wishlist> findAllByMemberId(Long memberId);

// 올바름
List<WishlistEntity> findAllByMemberId(Long memberId);
```

도메인 변환(`toDomain()`)은 `RepositoryImpl`에서 처리한다.

---

### Q. JPA `save()` 이후 원본 객체의 id가 왜 null인가?

JPA `save()`는 원본 객체를 수정하지 않고 **새로운 영속 객체를 반환**한다.

```java
Wishlist wishlist = new Wishlist(memberId, 1L);  // id = null
Wishlist saved = repository.save(wishlist);       // id = 1 (DB 생성)

wishlist.getId()  // null  ← 원본은 그대로
saved.getId()     // 1     ← 반환된 객체에 id 있음
```

`save()` 이후 항상 반환값을 사용해야 한다. 원본 객체를 계속 쓰면 id가 null이라 예상치 못한 버그가 발생한다.

---

### Q. 테이블명과 클래스명이 다르면 어떻게 매핑하나?

`@Table(name = "테이블명")`으로 명시적으로 지정한다.

```java
@Entity
@Table(name = "wishlist")       // 테이블명
public class WishlistEntity { } // 클래스명
```

테이블명과 클래스명이 일치하지 않아도 `@Table`로 매핑할 수 있다.
단, 일관성 측면에서 가급적 테이블명과 클래스명의 도메인 개념을 통일하는 것이 좋다.

---

### Q. soft delete JPQL에서 `LocalDateTime.now()`와 `CURRENT_TIMESTAMP` 중 무엇이 나은가?

`CURRENT_TIMESTAMP`(DB 서버 시간)가 더 안전하다.

- `LocalDateTime.now()` — 애플리케이션 서버 시간. 서버가 여러 대면 시간이 미세하게 다를 수 있음
- `CURRENT_TIMESTAMP` — DB 서버 시간 기준으로 통일. H2/MySQL 모두 JPQL 표준 함수

```java
@Modifying(clearAutomatically = true)
@Query("UPDATE WishlistEntity w SET w.deletedAt = CURRENT_TIMESTAMP WHERE w.id = :id")
void deleteById(@Param("id") Long id);
```

파라미터도 줄어들어 코드가 더 단순해진다.

---

## DB 설계 / ERD

### Q. DB 테이블명과 컬럼명은 대문자로 쓰나?

소문자 snake_case가 현업 표준이다.

- SQL 키워드(`CREATE TABLE`, `NOT NULL`, `AUTO_INCREMENT`) → 대문자
- 테이블명/컬럼명(`member`, `created_at`, `member_id`) → 소문자 snake_case

이유:
- MySQL은 Linux에서 대소문자를 구분하고 Mac/Windows에서는 무시한다. 소문자로 통일하면 OS 관계없이 동일하게 동작한다.
- Hibernate 기본 네이밍 전략이 카멜케이스 → snake_case 자동 변환이므로 대문자 컬럼명을 쓰면 따옴표 처리가 필요해져 복잡해진다.

---

### Q. BCrypt 비밀번호 컬럼을 VARCHAR(255)로 쓰는 게 맞나?

아니다. BCrypt는 항상 **고정 60자**를 출력하므로 `VARCHAR(60)`이 정확하다.

`VARCHAR(255)`는 길이를 모르는 사람이 관성적으로 설정한 신호다. 컬럼 타입은 비즈니스 규칙을 문서화하는 역할도 한다.

추후 BCrypt → Argon2 등 알고리즘 변경 시 Flyway `V2__` 파일로 대응한다.

| 알고리즘 | 출력 길이 | 권장 컬럼 |
|---------|---------|---------|
| BCrypt | 60자 고정 | `VARCHAR(60)` |
| Argon2 | ~95자 | `VARCHAR(128)` |

---

### Q. ERD에서 위시 리스트 관계는 어떻게 표현하나?

`wishlist` 테이블이 `member`와 `product` 사이의 N:M 관계를 풀어주는 중간 테이블(Junction Table)이다.

```
Member (1) ──────< Wishlist >────── (1) Product
```

- `member` : `wishlist` = 1:N (한 회원이 0개 이상의 위시 항목을 가짐)
- `product` : `wishlist` = 1:N (한 상품이 0개 이상의 위시 항목에 포함됨)
- `member` : `product` = N:M (wishlist를 통해 간접 연결)

DB에서 N:M은 직접 표현할 수 없다. 반드시 중간 테이블로 풀어야 한다.

---

## 문서화 / 용어 사전

### Q. 용어 사전은 README.md에 넣는 게 맞나?

README.md 하나에 다 넣는 건 비추다. README는 "처음 보는 사람을 위한 진입점"이므로 빌드/실행 방법, 기술 스택 등을 담아야 한다. 용어 사전까지 포함하면 둘 다 읽기 어려워진다.

`docs/domain-glossary.md` 별도 파일로 분리하고 README에서 링크만 연결한다.

```
docs/
└── domain-glossary.md
README.md  ← docs/ 링크만 포함
```

---

### Q. 용어 사전에 accessToken을 포함해야 하나?

포함해야 한다. 용어 사전은 코드 구조가 아닌 **비즈니스 개념**을 정의하는 문서다.

`accessToken`은 `Member` 도메인 객체의 필드가 아니지만, "로그인"이라는 비즈니스 행위의 결과물이며 기획자/프론트엔드 모두가 알아야 할 핵심 개념이다. `LoginResponse`라는 클래스명은 개발자만 아는 구현 세부사항이다.

---

### Q. 용어 사전은 단순 표만 있으면 충분한가?

아니다. 단순 표는 "용어 사전의 뼈대"일 뿐이다.

진짜 DDD 용어 사전이 갖춰야 할 요소:
1. **바운디드 컨텍스트 경계** — 같은 `memberId`라도 컨텍스트마다 목적이 다름
2. **용어 간 관계** — `Member`는 `Wish`를 소유, `Wish`는 `Product`를 참조
3. **사용 금지 용어 (Anti-language)** — `유저` → `회원`, `장바구니` → `위시 리스트`
4. **정책/제약사항 연결** — 단어 정의에서 끝내지 않고 비즈니스 규칙까지 연결

최소한 ① 바운디드 컨텍스트 경계 + ② 용어 테이블 + ③ 정책 연결 세 가지는 갖춰야 "DDD 용어 사전을 이해하고 작성했다"는 인상을 준다.

---

## 빌드 / 배포

### Q. `bootRun`으로 실행하면 `static/docs/index.html`이 없는 이유는?

Spring REST Docs 문서 생성 흐름이 `bootJar`에만 연결되어 있기 때문이다.

```
test → asciidoctor → bootJar → static/docs/ 복사
```

`bootRun`은 이 흐름과 무관하므로 HTML 파일이 복사되지 않는다.
REST Docs 문서를 확인하려면 `./gradlew build` 후 JAR로 실행해야 한다.

```bash
./gradlew build
java -jar build/libs/*-SNAPSHOT.jar
```

---

### Q. ktlint 빌드 시 `File must end with a newline` 오류가 나는 이유는?

UNIX 계열 텍스트 파일은 마지막 줄도 `\n`으로 끝나야 한다는 관례 때문이다.
ktlint의 `standard:final-newline` 규칙이 이를 강제한다.

AI 도구가 파일을 편집할 때 마지막 개행을 빠뜨리는 경우가 종종 발생한다.

IntelliJ에서 `Settings → Editor → General → "Ensure every saved file ends with a line break"`를 활성화하면 재발을 방지할 수 있다.

---

### Q. `./gradlew build` 시 JAR 파일이 2개 생기는데 어떤 것을 실행해야 하나?

`*-SNAPSHOT.jar`를 실행해야 한다.

| 파일 | 설명 |
|------|------|
| `*-SNAPSHOT.jar` | Spring Boot Fat JAR — 의존성 + 내장 톰캣 포함, 실행 가능 |
| `*-SNAPSHOT-plain.jar` | Gradle 기본 `jar` 태스크 산출물 — 의존성 없음, 실행 불가 |

`-plain.jar`는 라이브러리 배포용이다. 일반적으로 `bootJar { enabled = true }`, `jar { enabled = false }` 설정으로 plain JAR 생성을 비활성화하기도 한다.
