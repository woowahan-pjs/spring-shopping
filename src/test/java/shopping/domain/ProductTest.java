package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

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

    @ParameterizedTest
    @DisplayName("상품명 길이는 최소 1자리 ~ 최대 15자리다")
    @ValueSource(strings = {"상", "상품입니다", "상품입니다상품입니다상품입니다"})
    void productNameMaxLength(String name) {
        Product product = new Product(name);

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명이 비어있거나, 1자 미만이거나 15자를 초과하면 예외 발생")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "상품입니다상품입니다상품입니다상"})
    void invalidNameLength(String name) {
        assertThatThrownBy(() -> new Product(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품은 가격을 가진다")
    void productPrice() {
        BigDecimal price = new BigDecimal(15000);
        Product product = new Product("피자", price);

        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 가격이 음수면 실패한다.")
    void productPriceLessThanZero() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> new Product("피자", price))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("상품 가격이 10억 이상이면 실패한다.")
    void productPriceExceedsMax() {
        BigDecimal price = new BigDecimal(1000000000);

        assertThatThrownBy(() -> new Product("피자", price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이미지는 URL 경로로 표현한다.")
    void productImage() {
        String url = "https://www.naver.com";
        Product product = new Product("피자", new BigDecimal(15000), url);

        assertThat(product.getImageUrl()).isEqualTo(url);
    }
}