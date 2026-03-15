package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shopping.product.infrastructure.ProductRepositoryImpl;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static shopping.product.domain.ProductFixture.*;

@DataJpaTest
@Import(ProductRepositoryImpl.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

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
        Product found = repository.findById(saved.getId()).get();

        assertAll(
                () -> assertThat(found.getId()).isEqualTo(saved.getId()),
                () -> assertThat(found.getName()).isEqualTo(saved.getName()),
                () -> assertThat(found.getPrice()).isEqualTo(saved.getPrice()),
                () -> assertThat(found.getImageUrl()).isEqualTo(saved.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void update() {
        String name = "새로운 이름";
        Product saved = repository.save(createProduct());
        Product found = repository.findById(saved.getId()).get();
        found.changeName(name);

        Product updated = repository.update(found.getId(), found);

        assertThat(updated.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품은 삭제된다")
    void delete() {
        Product saved = repository.save(createProduct());

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void findAll() {

        List<Product> productList = repository.findAll();

        assertThat(productList.size()).isEqualTo(3);
    }
}
