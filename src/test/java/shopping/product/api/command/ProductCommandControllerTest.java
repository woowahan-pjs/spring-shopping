package shopping.product.api.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.api.command.dto.ProductRegisterRequest;
import shopping.product.service.FakeProductNameValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductCommandControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public FakeProductNameValidator fakeProductNameValidator() {
            return new FakeProductNameValidator();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FakeProductNameValidator productNameValidator;

    @BeforeEach
    void setUp() {
        productNameValidator.setProfane(false);
    }

    @Test
    @DisplayName("상품 생성 성공 시 201과 상품 정보를 반환한다")
    void test01() throws Exception {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").isNotEmpty())
               .andExpect(jsonPath("$.name").value("상품명"))
               .andExpect(jsonPath("$.price").value(10000))
               .andExpect(jsonPath("$.imageUrl").value("https://example.com/image.jpg"));
    }

    @Test
    @DisplayName("상품명이 15자를 초과하면 400을 반환한다")
    void test02() throws Exception {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("열여섯자이상의긴상품명입니다초과", 10000L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("상품명은 15자 이하이어야 합니다."));
    }

    @Test
    @DisplayName("상품명에 허용되지 않은 특수문자가 있으면 400을 반환한다")
    void test03() throws Exception {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명!", 10000L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("상품명에 허용되지 않은 특수문자가 포함되어 있습니다."));
    }

    @Test
    @DisplayName("상품명에 비속어가 포함되면 400을 반환한다")
    void test04() throws Exception {
        // given
        productNameValidator.setProfane(true);
        ProductRegisterRequest request = new ProductRegisterRequest("badword", 10000L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("상품명에 비속어가 포함되어 있습니다."));
    }

    @Test
    @DisplayName("가격이 0 이하이면 400을 반환한다")
    void test05() throws Exception {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명", 0L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("가격은 양수이어야 합니다."));
    }

    @Test
    @DisplayName("상품 수정 성공 시 200과 수정된 상품 정보를 반환한다")
    void test06() throws Exception {
        // given
        Long savedId = register(new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));
        ProductRegisterRequest request = new ProductRegisterRequest("수정된상품명", 20000L, "https://example.com/new.jpg");

        // when & then
        mockMvc.perform(put("/api/products/" + savedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("수정된상품명"))
               .andExpect(jsonPath("$.price").value(20000));
    }

    @Test
    @DisplayName("존재하지 않는 상품 수정 시 400을 반환한다")
    void test07() throws Exception {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg");

        // when & then
        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    @Test
    @DisplayName("상품 삭제 성공 시 204를 반환한다")
    void test08() throws Exception {
        // given
        Long savedId = register(new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));

        // when & then
        mockMvc.perform(delete("/api/products/" + savedId))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 400을 반환한다")
    void test09() throws Exception {
        mockMvc.perform(delete("/api/products/999"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    private Long register(ProductRegisterRequest request) throws Exception {
        String response = mockMvc.perform(post("/api/products")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
                                 .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }
}
