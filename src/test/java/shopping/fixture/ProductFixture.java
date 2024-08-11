package shopping.fixture;

import shopping.fake.FakeProfanityChecker;
import shopping.product.domain.Product;

public class ProductFixture {

    public static Product createProduct() {
        final FakeProfanityChecker profanityChecker = new FakeProfanityChecker();
        return new Product(1L, "맥북", profanityChecker, 1_000L, "image.jpg");
    }
}
