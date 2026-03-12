package shopping.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.product.domain.InMemoryProductRepository;
import shopping.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

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

    @Test
    @DisplayName("상품 조회한다")
    void findProductById() {
        Product product = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
        Product saved = productService.save(product);

        Product found = productService.findProductById(saved.getId());

        assertThat(found).isNotNull();
    }
    @Test
    @DisplayName("상품 목록을 조회한다")
    void findProducts() {
        Product product1 = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
        Product product2 = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
        Product product3 = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);

        productService.save(product1);
        productService.save(product2);
        productService.save(product3);

        List<Product> products = productService.findProducts();

        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 정보를 변경한다")
    void update() {
        Product product = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
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
        Product product = new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
        productService.save(product);

        productService.deleteById(product.getId());

        List<Product> products = productService.findProducts();

        assertThat(products).isEmpty();
    }
}