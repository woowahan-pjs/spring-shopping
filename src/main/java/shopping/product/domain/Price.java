package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false)
    private Long value;

    protected Price() {
    }

    public Price(Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("가격은 양수이어야 합니다.");
        }
        this.value = value;
    }

    public Long value() {
        return value;
    }
}
