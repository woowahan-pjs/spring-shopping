package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {
    private static final String VALID_NAME = "피자";
    private static final BigDecimal VALID_PRICE = BigDecimal.ZERO;
    private static final String VALID_IMAGE_URL = "http://a.com/a.jpg";

    private Product createProduct(String name) {
        return new Product(name, VALID_PRICE, VALID_IMAGE_URL);
    }

    private Product createProduct(String name, BigDecimal price) {
        return new Product(name, price, VALID_IMAGE_URL);
    }

    @Test
    @DisplayName("상품은 이름을 가진다")
    void productName() {
        Product product = createProduct(VALID_NAME);

        assertThat(product.getName()).isEqualTo(VALID_NAME);
    }

    @ParameterizedTest
    @DisplayName("상품명 길이는 최소 1자리 ~ 최대 15자리다")
    @ValueSource(strings = {"상", "상품입니다", "상품입니다상품입니다상품입니다"})
    void productNameMaxLength(String name) {
        Product product = createProduct(name);

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명이 비어있거나, 1자 미만이거나 15자를 초과하면 예외 발생")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "상품입니다상품입니다상품입니다상"})
    void invalidNameLength(String name) {
        assertThatThrownBy(() -> createProduct(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품명에는 특수문자 (), [], +, -, &, /, _ 사용할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"(", ")", "[", "]", "+", "-", "&", "/", "_"})
    void productName_withSpecialChars(String specialChar) {
        assertDoesNotThrow(() -> createProduct(specialChar));
    }

    @DisplayName("상품명에는 특수문자 (), [], +, -, &, /, _ 제외하면 사용할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"@", "!", "*", "#", "=", "{", "}", "\\", "|"})
    void productName_withDisallowedSpecialChars(String specialChar) {
        assertThatThrownBy(() -> createProduct(specialChar))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품은 가격을 가진다")
    void productPrice() {
        BigDecimal price = new BigDecimal(15000);
        Product product = createProduct(VALID_NAME, price);

        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 가격이 음수면 실패한다.")
    void productPriceLessThanZero() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> createProduct(VALID_NAME, price))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("상품 가격이 10억 이상이면 실패한다.")
    void productPriceExceedsMax() {
        BigDecimal price = new BigDecimal(1000000000);

        assertThatThrownBy(() -> createProduct(VALID_NAME, price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이미지는 URL 경로로 표현한다.")
    void productImage() {
        String url = "https://www.naver.com/test.jpg";
        Product product = new Product(VALID_NAME, VALID_PRICE, url);

        assertThat(product.getImageUrl()).isEqualTo(url);
    }


    @ParameterizedTest
    @DisplayName("상품이미지 경로가 잘못되면 예외처리")
    @NullAndEmptySource
    @ValueSource(strings = {"hhpp://naver.com", "dkjksjkjfd"})
    void invalidImageUrl(String url) {
        assertThatThrownBy(() -> new Product(VALID_NAME, VALID_PRICE, url))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("상품이미지 파일 확장자는 jpg, jpeg, png, gif, svg만 가능")
    @ValueSource(strings = {"http://a.com/a.jpg", "http://a.com/a.jpeg", "http://a.com/a.png", "http://a.com/a.gif", "http://a.com/a.svg"})
    void productImageUrl_withValidExtension(String url) {
        assertDoesNotThrow(() -> new Product(VALID_NAME, VALID_PRICE, url));
    }

    @ParameterizedTest
    @DisplayName("상품이미지 파일 확장자가 잘못되면 예외처리")
    @ValueSource(strings = {"http://a.com/a.txt", "http://a.com/a.mp3", "http://a.com/a", "http://a.com/a.pdf"})
    void productImageUrl_withInvalidExtension(String url) {
        assertThatThrownBy(() -> new Product(VALID_NAME, VALID_PRICE, url))
                .isInstanceOf(IllegalArgumentException.class);
    }
}