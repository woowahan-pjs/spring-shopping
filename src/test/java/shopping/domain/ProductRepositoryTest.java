package shopping.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

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

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void update() {
        String name = "새로운 이름";
        Product saved = productRepository.save(createProduct());
        Product found = productRepository.findById(saved.getId());

        found.changeName(name);

        Product updated = productRepository.update(found.getId(), found);

        assertThat(updated.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품은 삭제된다")
    void delete() {
        Product saved = productRepository.save(createProduct());

        productRepository.deleteById(saved.getId());

        Product deleted = productRepository.findById(saved.getId());

        assertThat(deleted).isNull();
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void findAll() {
        productRepository.save(createProduct());
        productRepository.save(createProduct());
        productRepository.save(createProduct());

        List<Product> productList = productRepository.findAll();

        assertThat(productList.size()).isEqualTo(3);
    }
}
