package shopping.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shopping.product.domain.InMemoryProductRepository;
import shopping.product.domain.Product;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static shopping.product.domain.ProductFixture.*;

@DisplayName("상품 서비스")
class ProductServiceTest {
    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new InMemoryProductRepository(), new FakeProfanityValidator());
    }

    @Test
    @DisplayName("상품명 비속어를 확인하고 저장한다.")
    void save() {
        Product product = createProduct();

        Product saved = productService.save(product);

        assertThat(saved).isNotNull();
    }


    @Test
    @DisplayName("상품명이 비속어면 예외발생")
    void invalidName() {
        Product product = createProduct("dickhead");

        assertThatThrownBy(() -> productService.save(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 조회한다")
    void findProductById() {
        Product product = createProduct();
        Product saved = productService.save(product);

        Product found = productService.findProductById(saved.getId());

        assertThat(found).isNotNull();
    }
    @Test
    @DisplayName("상품 목록을 조회한다")
    void findProducts() {
        Product product1 = createProduct();
        Product product2 = createProduct();
        Product product3 = createProduct();

        productService.save(product1);
        productService.save(product2);
        productService.save(product3);

        Page<Product> products = productService.findProducts(PageRequest.of(0, 20));

        assertThat(products.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("상품 정보를 변경한다")
    void update() {
        Product product = createProduct();
        productService.save(product);

        product.changeName("의자");
        product.changePrice(BigDecimal.valueOf(10000));
        productService.update(product.getId(), product);

        Product find = productService.findProductById(product.getId());

        assertThat(find.getName()).isEqualTo("의자");
        assertThat(find.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
    }

    @Test
    @DisplayName("상품을 삭제한다")
    void delete() {
        Product product = createProduct();
        productService.save(product);

        productService.deleteById(product.getId());

        Page<Product> products = productService.findProducts(PageRequest.of(0, 20));

        assertThat(products.isEmpty()).isTrue();
    }
}