package shopping.product.domain;

import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(name = "name", nullable = false, length = 15))
    private ProductName name;

    @Column(nullable = false)
    private long price;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    protected Product() {}

    public Product(ProductName name, long price, String imageUrl) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = name.isVerified() ? ProductStatus.CREATED : ProductStatus.PENDING;
    }

    public void update(ProductName name, long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = name.isVerified() ? ProductStatus.CREATED : ProductStatus.PENDING;
    }

    public UUID getId() {
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

    public ProductStatus getStatus() {
        return status;
    }
}
