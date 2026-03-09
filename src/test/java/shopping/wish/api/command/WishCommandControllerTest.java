package shopping.wish.api.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.AuthService;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.api.command.dto.WishAddRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("유효한 토큰으로 위시리스트 상품을 추가할 수 있다")
    void test01() throws Exception {
        // arrange
        Member member = memberRepository.save(Member.builder()
                .email("user@example.com")
                .password("password123")
                .build());
        Product product = productRepository.save(
                Product.builder().name("상품명").price(10000L).imageUrl("https://example.com/image.jpg").build()
        );
        String token = authService.createToken(member.getId());
        WishAddRequest request = new WishAddRequest(product.getId());

        // act & assert
        mockMvc.perform(post("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.productName").value("상품명"));
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 상품을 추가하면 401을 반환한다")
    void test02() throws Exception {
        // arrange
        WishAddRequest request = new WishAddRequest(1L);

        // act & assert
        mockMvc.perform(post("/api/wishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("인증 토큰이 없습니다."));
    }

    @Test
    @DisplayName("존재하지 않는 상품을 위시리스트에 추가하면 400을 반환한다")
    void test03() throws Exception {
        // arrange
        Member member = memberRepository.save(Member.builder()
                .email("user@example.com")
                .password("password123")
                .build());
        String token = authService.createToken(member.getId());
        WishAddRequest request = new WishAddRequest(999L);

        // act & assert
        mockMvc.perform(post("/api/wishes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }
}
