package shopping.domain.product;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private Price price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    protected Product() {
    }

    public Product(ProductName name, Price price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, BigDecimal price, String imageUrl) {
        this(new ProductName(name), new Price(price), imageUrl);
    }

    public static Product create(String name, BigDecimal price, String imageUrl) {
        return new Product(new ProductName(name), new Price(price), imageUrl);
    }

    public static Product create(ProductName name, Price price, String imageUrl) {
        return new Product(name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
