package shopping.product.application.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private String imagePath;
    private int amount;
    private long price;

    public ProductResponse() {
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
