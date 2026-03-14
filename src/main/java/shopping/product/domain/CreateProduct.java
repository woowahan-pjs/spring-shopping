package shopping.product.domain;

public interface CreateProduct {

    Product execute(String name, long price, String imageUrl);
}
