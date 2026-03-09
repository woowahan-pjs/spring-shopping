package shopping.wish.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.AuthService;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("유효한 토큰으로 위시리스트 목록을 조회할 수 있다")
    void test01() throws Exception {
        // arrange
        Member member = memberRepository.save(Member.builder()
                                                    .email("user@example.com")
                                                    .password("password123")
                                                    .build());
        Product product = productRepository.save(
                Product.builder().name("상품명").price(10000L).imageUrl("https://example.com/image.jpg").build()
        );
        wishRepository.save(Wish.builder().memberId(member.getId()).productId(product.getId()).build());
        String token = authService.createToken(member.getId());

        // act & assert
        mockMvc.perform(get("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].productName").value("상품명"))
                .andExpect(jsonPath("$[0].price").value(10000))
                .andExpect(jsonPath("$[0].imageUrl").value("https://example.com/image.jpg"));
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 목록을 조회하면 401을 반환한다")
    void test02() throws Exception {
        // arrange

        // act & assert
        mockMvc.perform(get("/api/wishes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("인증 토큰이 없습니다."));
    }
}
