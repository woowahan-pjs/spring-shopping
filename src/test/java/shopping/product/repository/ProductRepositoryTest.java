package shopping.product.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shopping.product.domain.Price;
import shopping.product.domain.Product;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품을 저장하고 ID로 조회할 수 있다")
    void test01() {
        // given
        Product product = Product.builder()
                                 .name("상품명")
                                 .price(new Price(10000L))
                                 .imageUrl("https://example.com/image.jpg")
                                 .build();

        // when
        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("전체 상품 목록을 조회할 수 있다")
    void test02() {
        // given
        productRepository.save(Product.builder().name("상품1").price(new Price(1000L)).imageUrl("https://example.com/1.jpg").build());
        productRepository.save(Product.builder().name("상품2").price(new Price(2000L)).imageUrl("https://example.com/2.jpg").build());

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
    }
}
