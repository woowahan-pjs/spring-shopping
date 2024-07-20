package shopping.product.fixture;

import shopping.product.domain.Product;

public class ProductFixture {

    public static Product createProduct(final Long id, final String name, final String imagePath, final int amount, final int price) {
        return new Product(id, name, imagePath, amount, price);
    }

    public static Product createProduct(final String name, final String imagePath, final int amount, final int price) {
        return new Product(null, name, imagePath, amount, price);
    }
}
