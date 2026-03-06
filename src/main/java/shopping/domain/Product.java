package shopping.domain;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;

public class Product {
    private String name;
    private BigDecimal price;
    private String imageUrl;

    public Product() {

    }

    public Product(String name) {
        this(name, BigDecimal.ZERO);
    }

    public Product(String name, BigDecimal price) {
        this(name, price, null);
    }

    public Product(String name, BigDecimal price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    private void validateImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("상품 이미지는 필수입니다.");
        }

        try {
            new URL(imageUrl).toURI();
        } catch (Exception e) {
            throw new IllegalArgumentException("상품 이미지 URL이 올바르지 않습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
