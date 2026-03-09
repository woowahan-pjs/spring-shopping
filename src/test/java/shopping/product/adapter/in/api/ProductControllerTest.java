package shopping.product.adapter.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;

    @Test
    @DisplayName("상품 생성은 201과 상품 응답을 돌려준다")
    void createReturnCreatedResponse() {
        // given
        ProductController controller = new ProductController(productService);
        ProductCreateRequest request = new ProductCreateRequest(
                "상품",
                "설명",
                new BigDecimal("10000"),
                "https://example.com/image.png"
        );
        ProductResponse response = new ProductResponse(
                1L,
                "상품",
                "설명",
                new BigDecimal("10000"),
                "https://example.com/image.png",
                0
        );
        when(productService.create(1L, request)).thenReturn(response);

        // when
        ResponseEntity<ProductResponse> result = controller.create(1L, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("상품 조회는 200과 상품 응답을 돌려준다")
    void getReturnOkResponse() {
        // given
        ProductController controller = new ProductController(productService);
        ProductResponse response = new ProductResponse(
                1L,
                "상품",
                "설명",
                new BigDecimal("10000"),
                "https://example.com/image.png",
                0
        );
        when(productService.get(1L)).thenReturn(response);

        // when
        ResponseEntity<ProductResponse> result = controller.get(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("상품 수정은 200과 상품 응답을 돌려준다")
    void updateReturnOkResponse() {
        // given
        ProductController controller = new ProductController(productService);
        ProductUpdateRequest request = new ProductUpdateRequest(
                "상품",
                "설명",
                new BigDecimal("10000"),
                "https://example.com/image.png"
        );
        ProductResponse response = new ProductResponse(
                1L,
                "상품",
                "설명",
                new BigDecimal("10000"),
                "https://example.com/image.png",
                0
        );
        when(productService.update(1L, 2L, request)).thenReturn(response);

        // when
        ResponseEntity<ProductResponse> result = controller.update(1L, 2L, request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("상품 삭제는 204를 돌려준다")
    void deleteReturnNoContent() {
        // given
        ProductController controller = new ProductController(productService);

        // when
        ResponseEntity<Void> result = controller.delete(1L, 2L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productService).delete(1L, 2L);
    }

    @Test
    @DisplayName("상품 목록 조회는 200과 목록을 돌려준다")
    void listReturnOkResponse() {
        // given
        ProductController controller = new ProductController(productService);
        List<ProductResponse> response = List.of(
                new ProductResponse(
                        1L,
                        "상품",
                        "설명",
                        new BigDecimal("10000"),
                        "https://example.com/image.png",
                        0
                )
        );
        when(productService.list()).thenReturn(response);

        // when
        ResponseEntity<List<ProductResponse>> result = controller.list();

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
