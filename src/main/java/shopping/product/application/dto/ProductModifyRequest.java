package shopping.product.application.dto;

public class ProductModifyRequest {
    private String name;
    private String imagePath;
    private int amount;
    private long price;

    public ProductModifyRequest() {
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
