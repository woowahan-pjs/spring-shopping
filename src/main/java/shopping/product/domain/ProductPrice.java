package shopping.product.domain;

import java.math.BigDecimal;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

public record ProductPrice(BigDecimal value) {
    public ProductPrice {
        if (value == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
        if (value.signum() <= 0) {
            throw new ApiException(ErrorCode.PRODUCT_PRICE_INVALID);
        }
    }
}
