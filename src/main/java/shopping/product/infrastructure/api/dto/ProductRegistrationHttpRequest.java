package shopping.product.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import shopping.product.application.command.ProductRegistrationCommand;

public class ProductRegistrationHttpRequest {

    private String name;
    private long amount;
    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;
    private long subCategoryId;

    public ProductRegistrationHttpRequest() {
    }

    public ProductRegistrationHttpRequest(final String name, final long amount, final String thumbnailImageUrl, final long subCategoryId) {
        this.name = name;
        this.amount = amount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.subCategoryId = subCategoryId;
    }

    public ProductRegistrationCommand toCommand(final long shopId, final long sellerId) {
        return new ProductRegistrationCommand(
                name,
                amount,
                thumbnailImageUrl,
                subCategoryId,
                shopId,
                sellerId
        );
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }
}
