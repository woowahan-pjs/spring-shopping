package shopping.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public static final String VALID_NAME = "피자";
    public static final BigDecimal VALID_PRICE = BigDecimal.ZERO;
    public static final String VALID_IMAGE_URL = "http://a.com/a.jpg";

    public static Product createProduct() {
        return new Product(VALID_NAME, VALID_PRICE, VALID_IMAGE_URL);
    }

    public static Product createWithId(Long id) {
        Product product = createProduct();
        product.assignId(id);
        return product;
    }
}
