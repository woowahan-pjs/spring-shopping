package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.product.exception.InvalidProductPriceException;

@DisplayName("ProductName")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductPriceTest {

    @Test
    void 가격이_null인_ProductPrice를_생성하면_예외를_던진다() {
        assertThatThrownBy(() -> new ProductPrice(null))
                .isInstanceOf(InvalidProductPriceException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L, -100_000L})
    void 가격이_0미만인_ProductPrice를_생성하면_예외를_던진다(long price) {
        assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(InvalidProductPriceException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 100L, 100_000L})
    void ProductPrice를_생성할_수_있다(long price) {
        assertThatNoException()
                .isThrownBy(() -> new ProductPrice(price));
    }
}
