package shopping.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static shopping.domain.ProductFixture.*;

public class ProductRepositoryTest {
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    @DisplayName("상품을 저장한다.")
    void save() {
        Product saved = repository.save(createProduct());

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void findById() {
        Product saved = repository.save(createProduct());

        Product found = repository.findById(saved.getId());

        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void update() {
        String name = "새로운 이름";
        Product saved = repository.save(createProduct());
        Product found = repository.findById(saved.getId());

        found.changeName(name);

        Product updated = repository.update(found.getId(), found);

        assertThat(updated.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품은 삭제된다")
    void delete() {
        Product saved = repository.save(createProduct());

        repository.deleteById(saved.getId());

        Product deleted = repository.findById(saved.getId());

        assertThat(deleted).isNull();
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void findAll() {
        repository.save(createProduct());
        repository.save(createProduct());
        repository.save(createProduct());

        List<Product> productList = repository.findAll();

        assertThat(productList.size()).isEqualTo(3);
    }
}
