package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductReadResponse {

    @JsonProperty("product_name")
    private String productName;
    private long amount;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("shop_name")
    private String shopName;

    @JsonProperty("seller_name")
    private String sellerName;

    public ProductReadResponse() {
    }

    public ProductReadResponse(final String productName, final long amount, final String imageUrl, final String categoryName, final String shopName, final String sellerName) {
        this.productName = productName;
        this.amount = amount;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
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
