package shopping.product.domain;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class Product {
    private static final int MAX_NAME_LENGTH = 15;
    private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(999_999_999);
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_\\s]+$";

    private Long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;

    public Product(String name, BigDecimal price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // DB 복원용.
    private Product(Long id, String name, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    void assignId(Long id) {
        this.id = id;
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public void changeImageUrl(String imageUrl) {
        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    public static Product of(Long id, String name, BigDecimal price, String imageUrl) {
        return new Product(id, name, price, imageUrl);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void update(Product product) {
        changeImageUrl(product.getImageUrl());
        changeName(product.getName());
        changePrice(product.getPrice());
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품명은 1자 이상 15자 이하여야 합니다.");
        }

        if (!name.matches(NAME_PATTERN)) {
            throw new IllegalArgumentException("상품명 특수문자는 (), [], +, -, &, /, _ 만 가능합니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("상품 가격은 필수입니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격은 0원 이상이어야 합니다.");
        }

        if (price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException("상품 가격은 10억원 이하여야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("상품 이미지는 필수입니다.");
        }

        try {
            URI uri = new URI(imageUrl);
            String scheme = uri.getScheme();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                throw new IllegalArgumentException("상품 이미지 URL은 http/https 형식이어야 합니다.");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("상품 이미지 URL이 올바르지 않습니다.");
        }

        String lowerCaseUrl = imageUrl.toLowerCase();
        if (!lowerCaseUrl.matches(".*\\.(jpg|jpeg|png|gif|svg)$")) {
            throw new IllegalArgumentException("이미지 파일은 jpg, jpeg, png, gif, svg 형식만 지원합니다.");
        }
    }
}
