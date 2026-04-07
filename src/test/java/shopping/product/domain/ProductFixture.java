package shopping.product.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public static final String VALID_NAME = "피자";
    public static final BigDecimal VALID_PRICE = BigDecimal.ZERO;
    public static final String VALID_IMAGE_URL = "http://a.com/a.jpg";

    public static Product createProduct() {
        return new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
    }

    public static Product createProduct(String name) {
        return new Product(name, VALID_PRICE, VALID_IMAGE_URL);
    }

    public static Product createProduct(BigDecimal price) {
        return new Product(VALID_NAME, price, VALID_IMAGE_URL);
    }

    public static Product createWithId(Long id) {
        return Product.of(id, VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
    }
}
