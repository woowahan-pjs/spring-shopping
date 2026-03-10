package shopping.product;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProductName {

    private String value;

    private boolean verified;

    protected ProductName() {}

    ProductName(String value, boolean verified) {
        this.value = value;
        this.verified = verified;
    }

    public String getValue() {
        return value;
    }

    public boolean isVerified() {
        return verified;
    }
}
