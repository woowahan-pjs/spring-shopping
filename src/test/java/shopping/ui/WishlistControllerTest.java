package shopping.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shopping.domain.member.Member;
import shopping.domain.member.Role;
import shopping.domain.product.Product;
import shopping.domain.repository.MemberRepository;
import shopping.domain.repository.ProductRepository;
import shopping.dto.WishlistRequest;
import shopping.infrastructure.auth.JwtTokenProvider;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishlistControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private String accessToken;
    private Member savedMember;
    private Product savedProduct;

    @Autowired
    public WishlistControllerTest(MockMvc mockMvc,
                                  ObjectMapper objectMapper,
                                  MemberRepository memberRepository,
                                  ProductRepository productRepository,
                                  JwtTokenProvider jwtTokenProvider) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @BeforeEach
    void setUp() {
        savedMember = memberRepository.save(Member.create("tester@test.com", "password123!"));
        savedProduct = productRepository.save(Product.create("테스트 상품", BigDecimal.valueOf(50000), "path/image1.jpg"));

        accessToken = "Bearer " + jwtTokenProvider.createToken(savedMember.getEmail(), Role.CONSUMER);
    }

    @Test
    @DisplayName("인증된 사용자는 위시리스트에 상품을 추가할 수 있다.")
    void addWishlist_api_success() throws Exception {
        WishlistRequest request = new WishlistRequest(savedProduct.getId(), savedProduct.getId());

        mockMvc.perform(post("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 조회를 시도하면 403 Forbidden을 반환한다.")
    void getWishlist_fail_unauthorized() throws Exception {
        mockMvc.perform(get("/api/wishes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("인증된 사용자는 자신의 위시리스트 목록을 조회할 수 있다.")
    void getWishlist_api_success() throws Exception {
        // 위시리스트에 미리 하나 담기
        mockMvc.perform(post("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new WishlistRequest(savedMember.getId(), savedProduct.getId()))));

        mockMvc.perform(get("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("테스트 상품"));
    }
}