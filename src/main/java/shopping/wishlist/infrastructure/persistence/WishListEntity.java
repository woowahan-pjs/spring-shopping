package shopping.wishlist.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "wish_lists")
@Entity
public class WishListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "customer_id")
    private long customerId;

    public WishListEntity() {
    }

    public WishListEntity(final long productId, final long customerId) {
        this.productId = productId;
        this.customerId = customerId;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WishListEntity that = (WishListEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
