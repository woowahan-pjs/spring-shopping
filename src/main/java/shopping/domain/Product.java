package shopping.domain;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class Product {
    private String name;
    private BigDecimal price;

    public Product() {

    }

    public Product(String name) {
        this(name, BigDecimal.ZERO);
    }

    public Product(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }

        if (name.length() > 15) {
            throw new IllegalArgumentException("상품명은 1자 이상 15자 이하여야 합니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격은 0원 이상이어야 합니다.");
        }

        if (price.compareTo(new BigDecimal(999999999)) > 0) {
            throw new IllegalArgumentException("상품 가격은 10억원 이하여야 합니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
