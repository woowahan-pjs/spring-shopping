package shopping.product;

public class Product {

    private Long id;
    private ProductName name;
    private long price;
    private String imageUrl;

    public Product(Long id, ProductName name, long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(ProductName name, long price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
