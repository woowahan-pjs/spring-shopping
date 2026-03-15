package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static shopping.product.domain.ProductFixture.*;

class ProductTest {
    private Product createProduct(String name) {
        return new Product(name, VALID_PRICE, VALID_IMAGE_URL);
    }

    private Product createProduct(BigDecimal price) {
        return new Product(VALID_NAME, price, VALID_IMAGE_URL);
    }

    @Test
    @DisplayName("상품은 이름을 가진다")
    void productName() {
        Product product = createProduct(VALID_PRICE);

        assertThat(product.getName()).isEqualTo(VALID_NAME);
    }

    @Test
    @DisplayName("상품명은 변경이 된다")
    void changeName() {
        String name = "변경";
        Product product = createProduct(VALID_PRICE);

        product.changeName(name);

        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품 가격은 변경이 된다")
    void changePrice() {
        BigDecimal changePrice = new BigDecimal(10000);
        Product product = createProduct(VALID_PRICE);

        product.changePrice(changePrice);

        assertThat(product.getPrice()).isEqualTo(changePrice);
    }

    @Test
    @DisplayName("상품 이미지 경로는 변경이 된다")
    void changeImageUrl() {
        String url = "https://www.naver.com/test.jpg";
        Product product = createProduct(VALID_PRICE);

        product.changeImageUrl(url);

        assertThat(product.getImageUrl()).isEqualTo(url);
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
        assertThatCode(() -> createProduct(specialChar))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품명에는 특수문자 (), [], +, -, &, /, _ 제외하면 사용할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"@", "!", "*", "#", "=", "{", "}", "\\", "|"})
    void productName_withDisallowedSpecialChars(String specialChar) {
        assertThatThrownBy(() -> createProduct(specialChar))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("상품은 가격을 가진다")
    @ValueSource(ints = {0, 15000, 999_999_999})
    void productPrice(int price) {
        BigDecimal actualPrice = new BigDecimal(price);
        Product product = createProduct(actualPrice);

        assertThat(product.getPrice()).isEqualByComparingTo(actualPrice);
    }

    @Test
    @DisplayName("상품 가격이 음수면 실패한다.")
    void productPriceLessThanZero() {
        BigDecimal price = new BigDecimal(-1);

        assertThatThrownBy(() -> createProduct(price))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("상품 가격이 10억 이상이면 실패한다.")
    void productPriceExceedsMax() {
        BigDecimal price = new BigDecimal(1000000000);

        assertThatThrownBy(() -> createProduct(price))
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
        assertThatCode(() -> new Product(VALID_NAME, VALID_PRICE, url))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("상품이미지 파일 확장자가 잘못되면 예외처리")
    @ValueSource(strings = {"http://a.com/a.txt", "http://a.com/a.mp3", "http://a.com/a", "http://a.com/a.pdf"})
    void productImageUrl_withInvalidExtension(String url) {
        assertThatThrownBy(() -> new Product(VALID_NAME, VALID_PRICE, url))
                .isInstanceOf(IllegalArgumentException.class);
    }
}