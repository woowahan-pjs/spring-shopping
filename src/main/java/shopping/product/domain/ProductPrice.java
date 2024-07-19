package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import shopping.product.exception.InvalidProductPriceException;

@Embeddable
public class ProductPrice {

    @Column(name = "product_price", nullable = false)
    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(final Long price) {
        if (price == null) {
            throw new InvalidProductPriceException("상품 금액은 필수값 입니다.");
        }

        if (price < 0) {
            throw new InvalidProductPriceException("상품 금액은 0원 이상이어야 합니다. " + price);
        }

        this.price = BigDecimal.valueOf(price);
    }
}
