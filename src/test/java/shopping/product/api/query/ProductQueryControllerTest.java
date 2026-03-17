package shopping.product.api.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProfanityChecker;
import shopping.product.api.command.dto.ProductRegisterRequest;
import shopping.product.service.FakeProfanityChecker;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductQueryControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProfanityChecker fakeProfanityClient() {
            return new FakeProfanityChecker();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FakeProfanityChecker profanityClient;

    @BeforeEach
    void setUp() {
        profanityClient.setProfane(false);
    }

    @Test
    @DisplayName("상품 단건 조회 성공 시 200과 상품 정보를 반환한다")
    void test01() throws Exception {
        // given
        Long savedId = register(new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));

        // when & then
        mockMvc.perform(get("/api/products/" + savedId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(savedId))
               .andExpect(jsonPath("$.name").value("상품명"))
               .andExpect(jsonPath("$.price").value(10000))
               .andExpect(jsonPath("$.imageUrl").value("https://example.com/image.jpg"));
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 400을 반환한다")
    void test02() throws Exception {
        mockMvc.perform(get("/api/products/999"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    @Test
    @DisplayName("상품 목록 조회 시 페이징 응답을 반환한다")
    void test03() throws Exception {
        // arrange
        register(new ProductRegisterRequest("상품1", 1000L, "https://example.com/1.jpg"));
        register(new ProductRegisterRequest("상품2", 2000L, "https://example.com/2.jpg"));

        // act & assert
        mockMvc.perform(get("/api/products")
                       .param("page", String.valueOf(PageRequest.of(0, 1).getPageNumber()))
                       .param("size", String.valueOf(PageRequest.of(0, 1).getPageSize())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content.length()").value(1))
               .andExpect(jsonPath("$.content[0].name").value("상품2"))
               .andExpect(jsonPath("$.page").value(0))
               .andExpect(jsonPath("$.size").value(1))
               .andExpect(jsonPath("$.totalElements").value(2))
               .andExpect(jsonPath("$.totalPages").value(2))
               .andExpect(jsonPath("$.hasNext").value(true))
               .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    private Long register(ProductRegisterRequest request) throws Exception {
        String response = mockMvc.perform(post("/api/products")
                                          .contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(request)))
                                 .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }
}
