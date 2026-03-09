package shopping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import shopping.domain.Product;
import shopping.dto.ProductRequest;
import shopping.dto.ProductResponse;
import shopping.service.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static shopping.domain.ProductFixture.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProductService service;

    @Test
    @DisplayName("상품을 추가한다.")
    void addProduct() throws JsonProcessingException {
        Product product = createWithId(1L);
        ProductRequest request = new ProductRequest(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);

        given(service.save(any())).willReturn(product);

        assertThat(mockMvcTester.post().uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(ProductResponse.class)
                .satisfies(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getName()).isEqualTo(VALID_NAME);
                    assertThat(response.getPrice()).isEqualTo(VALID_PRICE);
                    assertThat(response.getImageUrl()).isEqualTo(VALID_IMAGE_URL);
                });
    }

    @Test
    @DisplayName("상품 목록을 가져온다.")
    void findAll() {
        given(service.findProducts()).willReturn(List.of(createWithId(1L), createWithId(2L), createWithId(3L)));

        assertThat(mockMvcTester.get().uri("/products"))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(InstanceOfAssertFactories.list(ProductResponse.class))
                .hasSize(3);
    }

    @Test
    @DisplayName("특정 상품을 조회한다")
    void findById() {
        given(service.findProductById(1L)).willReturn(createWithId(1L));

        assertThat(mockMvcTester.get().uri("/products/1"))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.id", id -> assertThat(id).isEqualTo(1));
    }

    @Test
    @DisplayName("상품을 수정한다")
    void update() throws JsonProcessingException {
        Product product = createWithId(1L);
        product.changeName("치킨");

        ProductRequest request = new ProductRequest( "치킨", VALID_PRICE, VALID_IMAGE_URL);

        given(service.update(eq(1L), any())).willReturn(product);

        assertThat(mockMvcTester.put().uri("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .convertTo(ProductResponse.class)
                .satisfies(res -> {
                   assertThat(res.getId()).isEqualTo(1L);
                   assertThat(res.getName()).isEqualTo("치킨");
                });
    }

    @Test
    @DisplayName("상품을 삭제한다")
    void delete() {
        willDoNothing().given(service).deleteById(1L);

        assertThat(mockMvcTester.delete().uri("/products/1"))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}