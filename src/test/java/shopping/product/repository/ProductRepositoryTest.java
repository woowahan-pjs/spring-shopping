package shopping.product.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shopping.product.domain.Product;

import java.util.List;
import java.util.Optional;

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

    @Test
    void 특정_상품을_조회한다() {

        //given
        Product product = Product.create("아이폰", 1_000_000);
        productRepository.save(product);

        //when
        Optional<Product> result = productRepository.findById(product.getId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("아이폰");
        assertThat(result.get().getPrice()).isEqualTo(1_000_000);
    }

    @Test
    void 상품을_삭제한다() {

        //given
        Product product = Product.create("아이폰", 1_000_000);
        productRepository.save(product);

        Long productId = product.getId();

        //when
        productRepository.deleteById(productId);

        //then
        Optional<Product> result = productRepository.findById(productId);

        assertThat(result).isEmpty();
    }

    @Test
    void 상품_목록을_조회한다() {

        //given
        Product product1 = Product.create("아이폰", 1_000_000);
        Product product2 = Product.create("맥북", 2_000_000);

        productRepository.save(product1);
        productRepository.save(product2);

        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThat(products).hasSize(2);
    }
}
