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
import shopping.domain.member.Role;
import shopping.domain.product.Product;
import shopping.domain.repository.ProductRepository;
import shopping.dto.ProductRequest;
import shopping.infrastructure.auth.JwtTokenProvider;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;
    private ProductRepository productRepository;

    private String adminToken;
    private String userToken;
    private Product savedProduct;

    @Autowired
    public ProductControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider, ProductRepository productRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        savedProduct = productRepository.save(Product.create("정식 등록 상품", new BigDecimal("50000"), "path/image1.jpg"));

        adminToken = "Bearer " + jwtTokenProvider.createToken("admin@test.com", Role.ADMIN);
        userToken = "Bearer " + jwtTokenProvider.createToken("user@test.com", Role.CONSUMER);
    }

    @Test
    @DisplayName("일반 사용자가 상품 등록을 시도하면 403 Forbidden을 반환한다.")
    void create_product_fail_forbidden() throws Exception {
        // 일반 사용자 토큰 생성
        String consumerToken = "Bearer " + jwtTokenProvider.createToken("user@test.com", Role.CONSUMER);
        ProductRequest request = new ProductRequest("불법 등록 상품", new BigDecimal("10000"), "path/image1.jpg");

        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, consumerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 권한을 가진 사용자는 상품을 정상적으로 등록할 수 있다.")
    void create_product_admin_success() throws Exception {
        // 관리자 토큰 생성
        String adminToken = "Bearer " + jwtTokenProvider.createToken("admin@test.com", Role.ADMIN);
        ProductRequest request = new ProductRequest("정식 등록 상품2", new BigDecimal("50000"), "path/image1.jpg");

        mockMvc.perform(post("/api/products")
                        .header(HttpHeaders.AUTHORIZATION, adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("관리자는 상품 정보를 수정할 수 있다.")
    void update_product_admin_success() throws Exception {
        ProductRequest updateRequest = new ProductRequest("아이폰 15 Pro", new BigDecimal("250000"), "path/image2.jpg");

        mockMvc.perform(put("/api/products/" + savedProduct.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일반 사용자가 상품 수정을 시도하면 403 Forbidden을 반환한다.")
    void update_product_user_fail() throws Exception {
        ProductRequest updateRequest = new ProductRequest("아이폰 15 Pro", new BigDecimal("250000"), "path/image2.jpg");

        mockMvc.perform(put("/api/products/" + savedProduct.getId())
                        .header(HttpHeaders.AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("상품 상세 조회는 토큰 없이도 가능하다.")
    void get_product_detail_success() throws Exception {
        mockMvc.perform(get("/api/products/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정식 등록 상품"));
    }

    @Test
    @DisplayName("관리자는 상품을 삭제할 수 있다.")
    void delete_product_admin_success() throws Exception {
        mockMvc.perform(delete("/api/products/" + savedProduct.getId())
                        .header(HttpHeaders.AUTHORIZATION, adminToken))
                .andExpect(status().isNoContent());
    }
}