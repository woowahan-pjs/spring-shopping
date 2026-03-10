package shopping.wish.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.auth.BcryptPasswordEncoder;
import shopping.member.domain.MemberEntity;
import shopping.member.domain.Role;
import shopping.member.infrastructure.persistence.jpa.MemberJpaRepository;
import shopping.member.application.dto.MemberLoginRequest;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.domain.ProfanityChecker;
import shopping.wish.application.dto.WishAddRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private BcryptPasswordEncoder passwordEncoder;

    @MockBean
    private ProfanityChecker profanityChecker;

    private String userToken;
    private String adminToken;
    private Long productId;

    @BeforeEach
    void setUp() throws Exception {
        given(profanityChecker.containsProfanity(anyString())).willReturn(false);

        memberJpaRepository.save(
            new MemberEntity("admin@test.com", passwordEncoder.encode("password123"), Role.ADMIN));
        adminToken = loginAndGetToken("admin@test.com", "password123");

        memberJpaRepository.save(
            new MemberEntity("user@test.com", passwordEncoder.encode("password123"), Role.USER));
        userToken = loginAndGetToken("user@test.com", "password123");

        productId = createProduct("테스트상품", 10000, "https://example.com/image.jpg");
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        MemberLoginRequest loginRequest = new MemberLoginRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
            .path("data").path("token").asText();
    }

    private Long createProduct(String name, int price, String imageUrl) throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(name, price, imageUrl);
        MvcResult result = mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
            .path("data").path("id").asLong();
    }

    private Long addWish(Long productId) throws Exception {
        WishAddRequest request = new WishAddRequest(productId);
        MvcResult result = mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
            .path("data").path("wishId").asLong();
    }

    // ===== 위시리스트 추가 =====

    @Test
    @DisplayName("위시리스트 추가 성공 시 201을 반환한다")
    void add_success() throws Exception {
        WishAddRequest request = new WishAddRequest(productId);

        mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.wishId").isNotEmpty())
            .andExpect(jsonPath("$.data.productId").value(productId));
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 추가 시 401을 반환한다")
    void add_unauthorized() throws Exception {
        WishAddRequest request = new WishAddRequest(productId);

        mockMvc.perform(post("/api/wishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("존재하지 않는 상품을 위시리스트에 추가하면 404를 반환한다")
    void add_productNotFound() throws Exception {
        WishAddRequest request = new WishAddRequest(99999L);

        mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("같은 상품을 중복 추가하면 409를 반환한다")
    void add_duplicate() throws Exception {
        addWish(productId);

        WishAddRequest request = new WishAddRequest(productId);
        mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("productId가 null이면 400을 반환한다")
    void add_nullProductId() throws Exception {
        WishAddRequest request = new WishAddRequest(null);

        mockMvc.perform(post("/api/wishes")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // ===== 위시리스트 삭제 =====

    @Test
    @DisplayName("위시리스트 삭제 성공 시 200을 반환한다")
    void delete_success() throws Exception {
        Long wishId = addWish(productId);

        mockMvc.perform(delete("/api/wishes/" + wishId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(wishId));
    }

    @Test
    @DisplayName("존재하지 않는 위시를 삭제하면 404를 반환한다")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/wishes/99999")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("다른 유저의 위시를 삭제하면 404를 반환한다")
    void delete_otherMemberWish() throws Exception {
        Long wishId = addWish(productId);

        // 다른 유저 생성
        memberJpaRepository.save(
            new MemberEntity("other@test.com", passwordEncoder.encode("password123"), Role.USER));
        String otherToken = loginAndGetToken("other@test.com", "password123");

        mockMvc.perform(delete("/api/wishes/" + wishId)
                .header("Authorization", "Bearer " + otherToken))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 삭제 시 401을 반환한다")
    void delete_unauthorized() throws Exception {
        Long wishId = addWish(productId);

        mockMvc.perform(delete("/api/wishes/" + wishId))
            .andExpect(status().isUnauthorized());
    }

    // ===== 위시리스트 조회 =====

    @Test
    @DisplayName("위시리스트 조회 성공 시 200을 반환한다")
    void getAll_success() throws Exception {
        addWish(productId);
        Long productId2 = createProduct("상품2", 5000, "https://example.com/2.jpg");
        addWish(productId2);

        mockMvc.perform(get("/api/wishes")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("위시리스트가 비어있으면 빈 배열을 반환한다")
    void getAll_empty() throws Exception {
        mockMvc.perform(get("/api/wishes")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("토큰 없이 위시리스트 조회 시 401을 반환한다")
    void getAll_unauthorized() throws Exception {
        mockMvc.perform(get("/api/wishes"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("다른 유저의 위시리스트는 조회되지 않는다")
    void getAll_onlyMyWish() throws Exception {
        addWish(productId);

        memberJpaRepository.save(
            new MemberEntity("other@test.com", passwordEncoder.encode("password123"), Role.USER));
        String otherToken = loginAndGetToken("other@test.com", "password123");

        mockMvc.perform(get("/api/wishes")
                .header("Authorization", "Bearer " + otherToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(0));
    }
}
