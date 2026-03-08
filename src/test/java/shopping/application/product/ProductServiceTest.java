package shopping.application.product;

import jakarta.transaction.Transactional;
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
}
