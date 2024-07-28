package shopping.product.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "product_detailed_images")
@Entity
public class ProductDetailedImageEntity {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detailed_image_url")
    private String detailedImageUrl;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    public ProductDetailedImageEntity() {
    }

    public ProductDetailedImageEntity(final Long id, final String detailedImageUrl, final ProductEntity product) {
        this.id = id;
        this.detailedImageUrl = detailedImageUrl;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getDetailedImageUrl() {
        return detailedImageUrl;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(final ProductEntity productEntity) {
        this.product = productEntity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductDetailedImageEntity that = (ProductDetailedImageEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
