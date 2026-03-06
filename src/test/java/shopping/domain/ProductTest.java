package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품을 생성한다")
    void success_createProduct() {
        Product product = new Product();

        assertThat(product).isNotNull();
    }

    @Test
    @DisplayName("상품은 이름을 가진다")
    void success_productName() {
        String name = "피자";
        Product product = new Product(name);

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명 길이는 최소 1자리 ~ 최대 15자리다")
    @ValueSource(strings = {"상", "상품입니다", "상품입니다상품입니다상품입니다"})
    void success_productNameMaxLength(String name) {
        Product product = new Product(name);

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명이 비어있거나, 1자 미만이거나 15자를 초과하면 예외 발생")
    @ValueSource(strings = {"", "상품입니다상품입니다상품입니다상"})
    void fail_invalidNameLength(String name) {
        assertThatThrownBy(() -> new Product(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

}