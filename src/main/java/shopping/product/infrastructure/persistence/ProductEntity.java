package shopping.product.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "products")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private long amount;
    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;
    @Column(name = "sub_category_id")
    private long subCategoryId;
    @Column(name = "shop_id")
    private long shopId;
    @Column(name = "seller_id")
    private long sellerId;

    public ProductEntity() {
    }

    public ProductEntity(final Long id, final String name, final long amount, final String thumbnailImageUrl, final long subCategoryId, final long shopId, final long sellerId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.subCategoryId = subCategoryId;
        this.shopId = shopId;
        this.sellerId = sellerId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public long getShopId() {
        return shopId;
    }

    public long getSellerId() {
        return sellerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductEntity that = (ProductEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
