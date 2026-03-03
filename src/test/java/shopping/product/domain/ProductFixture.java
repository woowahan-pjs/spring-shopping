package shopping.product.domain;

import java.math.BigDecimal;

import org.springframework.test.util.ReflectionTestUtils;

public class ProductFixture {

    public static Product fixture(
            final Long id, final String name, final BigDecimal price, final String imageUrl) {
        Product product = new Product();

        ReflectionTestUtils.setField(product, "id", id);
        ReflectionTestUtils.setField(product, "name", name);
        ReflectionTestUtils.setField(product, "price", price);
        ReflectionTestUtils.setField(product, "imageUrl", imageUrl);
        ReflectionTestUtils.setField(product, "isUse", true);

        return product;
    }
}
