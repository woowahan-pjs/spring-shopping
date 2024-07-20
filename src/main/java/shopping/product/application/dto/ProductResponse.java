package shopping.product.application.dto;

import shopping.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private String imagePath;
    private int amount;
    private long price;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final String imagePath, final int amount, final long price) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.amount = amount;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getImagePath(), product.getAmount(), product.getPrice());
    }

    public Long getId() {
        return id;
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
