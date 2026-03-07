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

    @Test
    @DisplayName("상품은 이름을 가진다")
    void productName() {
        String name = "피자";
        Product product = new Product(name, BigDecimal.ZERO, "http://a.com/a.jpg");

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명 길이는 최소 1자리 ~ 최대 15자리다")
    @ValueSource(strings = {"상", "상품입니다", "상품입니다상품입니다상품입니다"})
    void productNameMaxLength(String name) {
        Product product = new Product(name, BigDecimal.ZERO, "http://a.com/a.jpg");

        assertThat(product.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @DisplayName("상품명이 비어있거나, 1자 미만이거나 15자를 초과하면 예외 발생")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "상품입니다상품입니다상품입니다상"})
    void invalidNameLength(String name) {
        assertThatThrownBy(() -> new Product(name, BigDecimal.ZERO, "http://a.com/a.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품명에는 특수문자 (), [], +, -, &, /, _ 사용할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"(", ")", "[", "]", "+", "-", "&", "/", "_"})
    void productName_withSpecialChars(String specialChar) {
        assertDoesNotThrow(() -> new Product(specialChar, new BigDecimal(0), "http://a.com/a.jpg"));
    }

    @DisplayName("상품명에는 특수문자 (), [], +, -, &, /, _ 제외하면 사용할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"@", "!", "*", "#", "=", "{", "}", "\\", "|"})
    void productName_withDisallowedSpecialChars(String specialChar) {
        assertThatThrownBy(() -> new Product(specialChar, BigDecimal.ZERO, "http://a.com/a.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품은 가격을 가진다")
    void productPrice() {
        BigDecimal price = new BigDecimal(15000);
        Product product = new Product("피자", price, "http://a.com/a.jpg");

        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 가격이 음수면 실패한다.")
    void productPriceLessThanZero() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> new Product("피자", price, "http://a.com/a.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("상품 가격이 10억 이상이면 실패한다.")
    void productPriceExceedsMax() {
        BigDecimal price = new BigDecimal(1000000000);

        assertThatThrownBy(() -> new Product("피자", price, "http://a.com/a.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이미지는 URL 경로로 표현한다.")
    void productImage() {
        String url = "https://www.naver.com/test.jpg";
        Product product = new Product("피자", new BigDecimal(15000), url);

        assertThat(product.getImageUrl()).isEqualTo(url);
    }


    @ParameterizedTest
    @DisplayName("상품이미지 경로가 잘못되면 예외처리")
    @NullAndEmptySource
    @ValueSource(strings = {"hhpp://naver.com", "dkjksjkjfd"})
    void invalidImageUrl(String url) {
        assertThatThrownBy(() -> new Product("피자", new BigDecimal(0), url))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("상품이미지 파일 확장자는 jpg, jpeg, png, gif, svg만 가능")
    @ValueSource(strings = {"http://a.com/a.jpg", "http://a.com/a.jpeg", "http://a.com/a.png", "http://a.com/a.gif", "http://a.com/a.svg"})
    void productImageUrl_withValidExtension(String url) {
        assertDoesNotThrow(() -> new Product("피자", new BigDecimal(0), url));
    }

    @ParameterizedTest
    @DisplayName("상품이미지 파일 확장자가 잘못되면 예외처리")
    @ValueSource(strings = {"http://a.com/a.txt", "http://a.com/a.mp3", "http://a.com/a", "http://a.com/a.pdf"})
    void productImageUrl_withInvalidExtension(String url) {
        assertThatThrownBy(() -> new Product("피자", new BigDecimal(0), url))
                .isInstanceOf(IllegalArgumentException.class);
    }
}