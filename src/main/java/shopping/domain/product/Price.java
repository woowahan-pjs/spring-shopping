package shopping.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shopping.domain.product.exception.InvalidPriceException;

import java.math.BigDecimal;

@Embeddable
public class Price {
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected Price() {

    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }


    private void validate(BigDecimal price) {
        if(price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }

    public BigDecimal getValue() {
        return this.price;
    }
}
