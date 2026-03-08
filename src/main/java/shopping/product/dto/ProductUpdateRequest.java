package shopping.product.dto;

public class ProductUpdateRequest {

    private String name;
    private long price;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
