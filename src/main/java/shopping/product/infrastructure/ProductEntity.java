package shopping.product.infrastructure;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import shopping.common.BaseEntity;
import shopping.product.domain.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@SQLRestriction("deleted_at IS NULL")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    protected ProductEntity() {}

    private ProductEntity(Long id, String name, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return Product.of(id, name, price, imageUrl);
    }

    public static ProductEntity from(Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public void update(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }
}
