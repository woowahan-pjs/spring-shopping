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
        if (price < 0) {
            throw new InvalidProductPriceException("상품 금액은 0원 이어야 한다.");
        }

        this.price = BigDecimal.valueOf(price);
    }
}
