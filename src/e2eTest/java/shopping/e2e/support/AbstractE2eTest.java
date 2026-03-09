package shopping.e2e.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import shopping.auth.domain.RefreshTokenRepository;
import shopping.common.ErrorResponse;
import shopping.auth.adapter.in.api.TokenResponse;
import shopping.member.adapter.in.api.LoginRequest;
import shopping.member.adapter.in.api.RegisterRequest;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;
import shopping.product.domain.Product;
import shopping.product.domain.ProductImageUrl;
import shopping.product.domain.ProductName;
import shopping.product.domain.ProductPrice;
import shopping.product.domain.ProductRepository;
import shopping.wish.domain.WishlistItemRepository;
import shopping.wish.domain.WishlistRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")
public abstract class AbstractE2eTest {
    private static final String REFRESH_COOKIE_NAME = "refreshToken";

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected WishlistRepository wishlistRepository;

    @Autowired
    protected WishlistItemRepository wishlistItemRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanBeforeEach() {
        cleanDatabase();
    }

    @AfterEach
    void cleanAfterEach() {
        cleanDatabase();
    }

    protected AuthSession registerMember(String email, String password) {
        ResponseEntity<String> response = post(
                "/api/members/register",
                new RegisterRequest(email, password),
                jsonHeaders()
        );
        assertStatus(response, HttpStatus.CREATED);
        return toAuthSession(response);
    }

    protected AuthSession login(String email, String password) {
        ResponseEntity<String> response = post(
                "/api/members/login",
                new LoginRequest(email, password),
                jsonHeaders()
        );
        assertStatus(response, HttpStatus.OK);
        return toAuthSession(response);
    }

    protected AuthSession refresh(AuthSession session) {
        HttpHeaders headers = jsonHeaders();
        headers.add(HttpHeaders.COOKIE, session.refreshCookie());
        ResponseEntity<String> response = post("/api/auth/refresh", null, headers);
        assertStatus(response, HttpStatus.OK);
        return toAuthSession(response);
    }

    protected ResponseEntity<String> post(String path, Object body, HttpHeaders headers) {
        return exchange(path, HttpMethod.POST, body, headers);
    }

    protected ResponseEntity<String> put(String path, Object body, HttpHeaders headers) {
        return exchange(path, HttpMethod.PUT, body, headers);
    }

    protected ResponseEntity<String> get(String path, HttpHeaders headers) {
        return exchange(path, HttpMethod.GET, null, headers);
    }

    protected ResponseEntity<String> delete(String path, HttpHeaders headers) {
        return exchange(path, HttpMethod.DELETE, null, headers);
    }

    protected <T> T readBody(ResponseEntity<String> response, Class<T> type) {
        try {
            return objectMapper.readValue(response.getBody(), type);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("응답 본문을 읽지 못한다.", exception);
        }
    }

    protected <T> T readBody(ResponseEntity<String> response, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(response.getBody(), typeReference);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("응답 본문을 읽지 못한다.", exception);
        }
    }

    protected ErrorResponse readError(ResponseEntity<String> response) {
        return readBody(response, ErrorResponse.class);
    }

    protected HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    protected HttpHeaders bearerHeaders(String accessToken) {
        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    protected TestAccount seedSeller() {
        String password = "sellerPass123!";
        String email = uniqueEmail("seller");
        Member seller = Member.createSeller(email, passwordEncoder.encode(password));
        Member savedSeller = memberRepository.save(seller);
        return new TestAccount(savedSeller.getId(), email, password);
    }

    protected Product seedProduct(Long sellerId, String name) {
        Product product = Product.create(
                new ProductName(name),
                "기본 설명",
                new ProductImageUrl("https://example.com/image.png"),
                new ProductPrice(new BigDecimal("10000")),
                sellerId
        );
        return productRepository.save(product);
    }

    protected String uniqueEmail(String prefix) {
        return prefix + "-" + UUID.randomUUID() + "@example.com";
    }

    protected void assertStatus(ResponseEntity<String> response, HttpStatus expectedStatus) {
        Assertions.assertThat(response.getStatusCode())
                .withFailMessage(() -> summarize(response))
                .isEqualTo(expectedStatus);
    }

    protected ResponseEntity<String> exchange(String path, HttpMethod method, Object body, HttpHeaders headers) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url(path), method, requestEntity, String.class);
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private AuthSession toAuthSession(ResponseEntity<String> response) {
        TokenResponse tokenResponse = readBody(response, TokenResponse.class);
        String refreshCookie = extractRefreshCookie(response.getHeaders());
        return new AuthSession(tokenResponse.token(), refreshCookie);
    }

    private String extractRefreshCookie(HttpHeaders headers) {
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        if (cookies == null) {
            throw new IllegalStateException("refresh cookie가 없다.");
        }
        return cookies.stream()
                .map(this::toCookiePair)
                .filter(cookie -> cookie.startsWith(REFRESH_COOKIE_NAME + "="))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("refresh cookie가 없다."));
    }

    private String toCookiePair(String setCookieHeader) {
        int separatorIndex = setCookieHeader.indexOf(';');
        if (separatorIndex < 0) {
            return setCookieHeader;
        }
        return setCookieHeader.substring(0, separatorIndex);
    }

    private void cleanDatabase() {
        wishlistItemRepository.deleteAllInBatch();
        wishlistRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        refreshTokenRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    private String summarize(ResponseEntity<String> response) {
        return "status=" + response.getStatusCode()
                + ", headers=" + maskHeaders(response.getHeaders())
                + ", body=" + maskBody(response.getBody());
    }

    private HttpHeaders maskHeaders(HttpHeaders headers) {
        HttpHeaders maskedHeaders = new HttpHeaders();
        maskedHeaders.putAll(headers);
        if (maskedHeaders.containsKey(HttpHeaders.SET_COOKIE)) {
            maskedHeaders.put(
                    HttpHeaders.SET_COOKIE,
                    maskedHeaders.get(HttpHeaders.SET_COOKIE).stream()
                            .map(this::maskCookie)
                            .toList()
            );
        }
        return maskedHeaders;
    }

    private String maskBody(String body) {
        if (body == null) {
            return null;
        }
        return body.replaceAll("(\"token\"\\s*:\\s*\")([^\"]+)(\")", "$1***$3");
    }

    private String maskCookie(String cookie) {
        return cookie.replaceAll("(^[^=]+=)([^;]+)", "$1***");
    }
}
