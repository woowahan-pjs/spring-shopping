package shopping.application.product;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shopping.domain.product.Product;
import shopping.domain.repository.ProductRepository;
import shopping.dto.ProductRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("상품을 DB에 저장한다.")
    void should_save_product_to_database() {
        // given
        ProductRequest request = new ProductRequest("테스트 상품", new BigDecimal("10000"), "테스트 이미지 URL");

        // when
        Long savedId = productService.createProduct(request);

        // then
        Product foundProduct = productRepository.findById(savedId)
                .orElseThrow(() -> new AssertionError("상품이 DB에 저장되지 않았습니다. ID: " + savedId));

        assertThat(foundProduct.getName()).isEqualTo("테스트 상품");
    }

    @Test
    @DisplayName("상품 수정 요청이 들어오면 상품의 이름, 가격, 이미지가 변경되어야 한다.")
    void should_update_product_details() {
        // given: 기존 상품 등록
        ProductRequest createRequest = new ProductRequest("초기 상품", new BigDecimal("10000"), "old_url");
        Long productId = productService.createProduct(createRequest);

        // 수정할 데이터 준비
        ProductRequest updateRequest = new ProductRequest(
                "수정된 상품명",
                new BigDecimal("15000"),
                "new_url"
        );

        productService.updateProduct(productId, updateRequest);

        // then: 수정된 내용 검증
        Product updatedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AssertionError("상품을 찾을 수 없습니다."));

        assertThat(updatedProduct.getName()).isEqualTo("수정된 상품명");
        assertThat(updatedProduct.getPrice()).isEqualByComparingTo(new BigDecimal("15000"));
        assertThat(updatedProduct.getImageUrl()).isEqualTo("new_url");
    }
}
