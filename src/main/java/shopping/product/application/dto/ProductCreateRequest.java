package shopping.product.application.dto;

import shopping.product.domain.Product;

public class ProductCreateRequest {
    private String name;
    private String imagePath;
    private int amount;
    private long price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final String imagePath, final int amount, final int price) {
        this.name = name;
        this.imagePath = imagePath;
        this.amount = amount;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, imagePath, amount, price);
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
