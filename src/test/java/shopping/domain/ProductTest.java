package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품을 생성한다")
    void createProduct() {
        Product product = new Product();

        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("상품은 이름을 가진다")
    void productName() {
        String name = "피자";
        Product product = new Product(name);

        assertThat(product.getName()).isEqualTo(name);
    }
}