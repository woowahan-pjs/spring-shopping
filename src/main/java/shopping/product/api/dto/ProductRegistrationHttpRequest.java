package shopping.product.api.dto;

import shopping.product.application.command.ProductRegistrationCommand;

public class ProductRegistrationHttpRequest {

    private String name;
    private long amount;
    private String imageUrl;
    private long subCategoryId;

    public ProductRegistrationHttpRequest() {
    }

    public ProductRegistrationHttpRequest(final String name, final long amount, final String imageUrl, final long subCategoryId) {
        this.name = name;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.subCategoryId = subCategoryId;
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

    public ProductRegistrationCommand toCommand(final long shopId, final long sellerId) {
        return new ProductRegistrationCommand(
                name,
                amount,
                imageUrl,
                subCategoryId,
                shopId,
                sellerId
        );
    }
}
