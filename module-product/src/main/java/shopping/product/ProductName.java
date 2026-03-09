package shopping.product;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProductName {

    private String value;

    protected ProductName() {}

    ProductName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
