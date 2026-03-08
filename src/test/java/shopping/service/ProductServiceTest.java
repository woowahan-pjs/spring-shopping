package shopping.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.domain.InMemoryProductRepository;
import shopping.domain.Product;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class ProductServiceTest {
    public static final String VALID_NAME = "피자";
    public static final BigDecimal VALID_PRICE = BigDecimal.ZERO;
    public static final String VALID_IMAGE_URL = "http://a.com/a.jpg";

    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new InMemoryProductRepository(), new FakeProfanityValidator());
    }

    @Test
    @DisplayName("상품명 비속어를 확인하고 저장한다.")
    void save() {
        Product product = new Product("pizza", VALID_PRICE, VALID_IMAGE_URL);

        Product saved = productService.save(product);

        assertThat(saved).isNotNull();
    }


    @Test
    @DisplayName("상품명이 비속어면 예외발생")
    void invalidName() {
        Product product = new Product("dickhead", VALID_PRICE, VALID_IMAGE_URL);

        assertThatThrownBy(() -> productService.save(product))
                .isInstanceOf(IllegalArgumentException.class);
    }



}