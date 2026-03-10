package shopping.product.presentation;

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
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductUpdateRequest;
import shopping.product.domain.ProfanityChecker;
import shopping.member.application.dto.MemberLoginRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

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

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
        given(profanityChecker.containsProfanity(anyString())).willReturn(false);

        // ADMIN 유저 생성 및 토큰 취득
        memberJpaRepository.save(
            new MemberEntity("admin@test.com", passwordEncoder.encode("password123"), Role.ADMIN));
        adminToken = loginAndGetToken("admin@test.com", "password123");

        // 일반 USER 생성 및 토큰 취득
        memberJpaRepository.save(
            new MemberEntity("user@test.com", passwordEncoder.encode("password123"), Role.USER));
        userToken = loginAndGetToken("user@test.com", "password123");
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        MemberLoginRequest loginRequest = new MemberLoginRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        return objectMapper.readTree(body).path("data").path("token").asText();
    }

    // ===== 상품 생성 =====

    @Test
    @DisplayName("관리자가 상품을 생성하면 201을 반환한다")
    void create_success() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("테스트상품", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.name").value("테스트상품"))
            .andExpect(jsonPath("$.data.price").value(10000))
            .andExpect(jsonPath("$.data.imageUrl").value("https://example.com/image.jpg"));
    }

    @Test
    @DisplayName("일반 유저가 상품 생성 시 403을 반환한다")
    void create_forbidden_user() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("테스트상품", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("토큰 없이 상품 생성 시 401을 반환한다")
    void create_unauthorized() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("테스트상품", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("상품명이 빈값이면 400을 반환한다")
    void create_blankName() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품명이 15자 초과면 400을 반환한다")
    void create_nameTooLong() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("열여섯글자이상이면안됩니다!!!!", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("가격이 음수이면 400을 반환한다")
    void create_negativePrice() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("상품명", -1,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품명에 비속어가 포함되면 400을 반환한다")
    void create_profanity() throws Exception {
        given(profanityChecker.containsProfanity("badword")).willReturn(true);
        ProductCreateRequest request = new ProductCreateRequest("badword", 10000,
            "https://example.com/image.jpg");

        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // ===== 상품 수정 =====

    @Test
    @DisplayName("관리자가 상품을 수정하면 200을 반환한다")
    void update_success() throws Exception {
        Long productId = createProduct("원래상품", 5000, "https://example.com/old.jpg");

        ProductUpdateRequest updateRequest = new ProductUpdateRequest("수정상품", 9000,
            "https://example.com/new.jpg");

        mockMvc.perform(patch("/api/products/" + productId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value("수정상품"))
            .andExpect(jsonPath("$.data.price").value(9000));
    }

    @Test
    @DisplayName("존재하지 않는 상품 수정 시 404를 반환한다")
    void update_notFound() throws Exception {
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("수정상품", 9000, null);

        mockMvc.perform(patch("/api/products/99999")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("일반 유저가 상품 수정 시 403을 반환한다")
    void update_forbidden() throws Exception {
        Long productId = createProduct("원래상품", 5000, "https://example.com/image.jpg");
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("수정상품", null, null);

        mockMvc.perform(patch("/api/products/" + productId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("수정 시 상품명에 비속어가 포함되면 400을 반환한다")
    void update_profanity() throws Exception {
        Long productId = createProduct("원래상품", 5000, "https://example.com/image.jpg");
        given(profanityChecker.containsProfanity("badword")).willReturn(true);
        ProductUpdateRequest updateRequest = new ProductUpdateRequest("badword", null, null);

        mockMvc.perform(patch("/api/products/" + productId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isBadRequest());
    }

    // ===== 상품 삭제 =====
    @Test
    @DisplayName("관리자가 상품을 삭제하면 200을 반환한다")
    void delete_success() throws Exception {
        Long productId = createProduct("삭제상품", 1000, "https://example.com/image.jpg");

        mockMvc.perform(delete("/api/products/" + productId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(productId));
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 404를 반환한다")
    void delete_notFound() throws Exception {
        mockMvc.perform(delete("/api/products/99999")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("삭제된 상품을 다시 삭제하면 404를 반환한다")
    void delete_alreadyDeleted() throws Exception {
        Long productId = createProduct("삭제상품", 1000, "https://example.com/image.jpg");
        mockMvc.perform(delete("/api/products/" + productId)
            .header("Authorization", "Bearer " + adminToken));

        mockMvc.perform(delete("/api/products/" + productId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("일반 유저가 상품 삭제 시 403을 반환한다")
    void delete_forbidden() throws Exception {
        Long productId = createProduct("삭제상품", 1000, "https://example.com/image.jpg");

        mockMvc.perform(delete("/api/products/" + productId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    // ===== 상품 단건 조회 =====

    @Test
    @DisplayName("상품 단건 조회 성공 시 200을 반환한다")
    void getById_success() throws Exception {
        Long productId = createProduct("조회상품", 3000, "https://example.com/image.jpg");

        mockMvc.perform(get("/api/products/" + productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(productId))
            .andExpect(jsonPath("$.data.name").value("조회상품"))
            .andExpect(jsonPath("$.data.price").value(3000));
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 404를 반환한다")
    void getById_notFound() throws Exception {
        mockMvc.perform(get("/api/products/99999"))
            .andExpect(status().isNotFound());
    }

    // ===== 상품 전체 조회 =====

    @Test
    @DisplayName("상품 전체 조회 성공 시 200을 반환한다")
    void getAll_success() throws Exception {
        createProduct("상품1", 1000, "https://example.com/1.jpg");
        createProduct("상품2", 2000, "https://example.com/2.jpg");

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    // ===== 헬퍼 메서드 =====
    private Long createProduct(String name, int price, String imageUrl) throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(name, price, imageUrl);
        MvcResult result = mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        return objectMapper.readTree(body).path("data").path("id").asLong();
    }
}
