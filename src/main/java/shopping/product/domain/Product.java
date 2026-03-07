package shopping.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int stockQuantity;

    private long price;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected Product() {

    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Product(String name, int stockQuantity, long price, String imageUrl) {
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product create(String name, long price) {
        return new Product(name, 0, price, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
