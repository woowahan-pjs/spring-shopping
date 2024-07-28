package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDetailResponse {
    @JsonProperty("product_name")
    private String productName;
    private long amount;

    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("shop_name")
    private String shopName;

    @JsonProperty("seller_name")
    private String sellerName;

    public ProductDetailResponse() {
    }

    public ProductDetailResponse(final String productName, final long amount, final String thumbnailImageUrl, final String categoryName, final String shopName, final String sellerName) {
        this.productName = productName;
        this.amount = amount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.categoryName = categoryName;
        this.shopName = shopName;
        this.sellerName = sellerName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public String getShopName() {
        return shopName;
    }

    public String getSellerName() {
        return sellerName;
    }
}

