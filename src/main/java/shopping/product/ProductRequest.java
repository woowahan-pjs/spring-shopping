package shopping.product;

public record ProductRequest(String name, long price, String imageUrl) {

    public Product toProduct() {
        return new Product(name, price, imageUrl);
    }
}
