package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductList {
    @JsonProperty("product_name")
    private String productName;
    private long amount;
    @JsonProperty("image_url")
    private String imageUrl;

    public ProductList() {
    }

    public ProductList(final String productName, final long amount, final String imageUrl) {
        this.productName = productName;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public long getAmount() {
        return amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
