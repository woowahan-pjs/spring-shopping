package shopping.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.domain.product.exception.InvalidPriceException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {
    @Test
    @DisplayName("가격이 null이거나 0원 미만이면 예외가 발생한다.")
    void createPriceWithNegativeValue() {
        BigDecimal negativeValue = new BigDecimal("-1");

        assertThrows(InvalidPriceException.class, () -> new Price(null));
        assertThrows(InvalidPriceException.class, () -> new Price(negativeValue));
    }
}