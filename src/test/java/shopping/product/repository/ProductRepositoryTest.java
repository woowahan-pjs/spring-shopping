package shopping.product.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shopping.product.domain.Product;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void 상품을_저장한다() {

        //given
        Product product = Product.create("아이폰", 1_000_000);

        //when
        Product savedProduct = productRepository.save(product);

        //then
        assertThat(savedProduct.getName()).isEqualTo("아이폰");
    }
}
