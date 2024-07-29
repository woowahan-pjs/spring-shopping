package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductInfo {
    @JsonProperty("product_name")
    private String productName;
    private long amount;
    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;

    public ProductInfo() {
    }

    public ProductInfo(final String productName, final long amount, final String thumbnailImageUrl) {
        this.productName = productName;
        this.amount = amount;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public long getAmount() {
        return amount;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }
}
