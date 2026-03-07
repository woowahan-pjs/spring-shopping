package shopping.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;
import static shopping.domain.ProductFixture.*;

public class ProductRepositoryTest {
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    @DisplayName("상품을 저장한다.")
    void save() {
        Product saved = productRepository.save(createProduct());

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void findById() {
        Product saved = productRepository.save(createProduct());

        Product found = productRepository.findById(saved.getId());

        assertThat(found).isEqualTo(saved);
    }
}
