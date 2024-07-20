package shopping.product.application.dto;

import shopping.product.domain.Product;

public class ProductRequest {
    private String name;
    private String imagePath;
    private int amount;
    private long price;

    public ProductRequest() {
    }

    public static Product toEntity(final ProductRequest createRequest) {
        return new Product(createRequest.name, createRequest.imagePath, createRequest.amount, createRequest.price);
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getAmount() {
        return amount;
    }

    public long getPrice() {
        return price;
    }
}
