package shopping.idempotency;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class IdempotencyFilterTest {

    private IdempotencyFilter filter;
    private InMemoryIdempotencyKeyRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIdempotencyKeyRepository();
        filter = new IdempotencyFilter(repository);
    }

    @Test
    void POST_요청에_Idempotency_Key가_없으면_400을_반환한다() throws Exception {
        var request = new MockHttpServletRequest("POST", "/api/products");
        var response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).contains("Idempotency-Key 헤더가 필요합니다.");
    }

    @Test
    void POST_요청에_Idempotency_Key가_있으면_정상_처리된다() throws Exception {
        var request = new MockHttpServletRequest("POST", "/api/products");
        request.addHeader("Idempotency-Key", "test-key-1");
        var response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void 같은_Idempotency_Key로_두번_요청하면_409를_반환한다() throws Exception {
        var request1 = new MockHttpServletRequest("POST", "/api/products");
        request1.addHeader("Idempotency-Key", "dup-key");
        var response1 = new MockHttpServletResponse();
        filter.doFilterInternal(request1, response1, new MockFilterChain());

        var request2 = new MockHttpServletRequest("POST", "/api/products");
        request2.addHeader("Idempotency-Key", "dup-key");
        var response2 = new MockHttpServletResponse();
        filter.doFilterInternal(request2, response2, new MockFilterChain());

        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(response2.getContentAsString()).contains("이미 처리된 요청입니다.");
    }

    @Test
    void GET_요청은_필터를_적용하지_않는다() {
        var request = new MockHttpServletRequest("GET", "/api/products");

        assertThat(filter.shouldNotFilter(request)).isTrue();
    }

    @Test
    void 상품_API가_아닌_POST_요청은_필터를_적용하지_않는다() {
        var request = new MockHttpServletRequest("POST", "/api/members/register");

        assertThat(filter.shouldNotFilter(request)).isTrue();
    }
}
