package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@DisplayName("[상품] 상품 값 객체 단위 테스트")
class ProductValueObjectsTest {
    @Test
    @DisplayName("유효한 상품 값 객체를 만들 수 있다")
    void createValidProductValueObjects() {
        ProductName productName = new ProductName("상품명");
        ProductPrice productPrice = new ProductPrice(new BigDecimal("10000"));
        ProductImageUrl productImageUrl = new ProductImageUrl("https://example.com/image.png");
        ProductDetails productDetails = new ProductDetails(
                productName,
                "  ",
                productImageUrl,
                productPrice
        );

        assertThat(productName.value()).isEqualTo("상품명");
        assertThat(productPrice.value()).isEqualByComparingTo("10000");
        assertThat(productImageUrl.value()).isEqualTo("https://example.com/image.png");
        assertThat(productDetails.description()).isNull();
    }

    @ParameterizedTest(name = "[{index}] 상품명={0}")
    @ValueSource(strings = {"abcdefghijklmnop", "가가가가가가가가가가가가가가가가"})
    @DisplayName("상품 이름이 15자를 넘으면 값 객체를 만들지 않는다")
    void rejectTooLongProductName(String name) {
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NAME_TOO_LONG);
    }

    @ParameterizedTest(name = "[{index}] 상품명={0}")
    @ValueSource(strings = {"상품!", "name*", "상품@"})
    @DisplayName("허용하지 않은 특수문자가 있으면 상품 이름 값을 만들지 않는다")
    void rejectDisallowedCharacters(String name) {
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS);
    }

    @ParameterizedTest(name = "[{index}] 가격={0}")
    @ValueSource(strings = {"0", "-1"})
    @DisplayName("가격이 0 이하이면 상품 가격 값을 만들지 않는다")
    void rejectInvalidPrice(String price) {
        assertThatThrownBy(() -> new ProductPrice(new BigDecimal(price)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_PRICE_INVALID);
    }

    @Test
    @DisplayName("절대 URL이 아니면 상품 이미지 URL 값을 만들지 않는다")
    void rejectRelativeImageUrl() {
        assertThatThrownBy(() -> new ProductImageUrl("/image.png"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_IMAGE_URL_NOT_ABSOLUTE);
    }

    @Test
    @DisplayName("URL 형식이 잘못되면 상품 이미지 URL 값을 만들지 않는다")
    void rejectInvalidImageUrlFormat() {
        assertThatThrownBy(() -> new ProductImageUrl("ht^tp://bad-url"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_IMAGE_URL_INVALID_FORMAT);
    }
}
