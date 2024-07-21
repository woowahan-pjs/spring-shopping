package shopping.product.api.dto;

public class ProductRegistrationHttpRequest {

    private long subCategoryId;
    private String name;
    private long amount;
    private String imageUrl;

    public ProductRegistrationHttpRequest() {
    }

    public ProductRegistrationHttpRequest(final long subCategoryId, final String name, final long amount, final String imageUrl) {
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.amount = amount;
        this.imageUrl = imageUrl;
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
}
