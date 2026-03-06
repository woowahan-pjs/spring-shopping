package shopping.domain;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class Product {
    private String name;
    private BigDecimal price;

    public Product() {

    }

    public Product(String name) {
        validNameLength(name);
        this.name = name;
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    private void validNameLength(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }

        if (name.length() > 15) {
            throw new IllegalArgumentException("상품명은 1자 이상 15자 이하여야 합니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
