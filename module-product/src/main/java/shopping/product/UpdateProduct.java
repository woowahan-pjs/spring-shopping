package shopping.product;

public interface UpdateProduct {

    Product execute(Long id, String name, long price, String imageUrl);
}
