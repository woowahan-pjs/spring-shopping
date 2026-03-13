package shopping.wishlist.infrastructure;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import shopping.common.BaseEntity;
import shopping.wishlist.domain.Wishlist;

@Entity
@Table(name = "wishlist")
@SQLRestriction("deleted_at IS NULL")
public class WishlistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    protected WishlistEntity() {}

    private WishlistEntity(Long id, Long memberId, Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
    }

    public static WishlistEntity from(Wishlist wishlist) {
        return new WishlistEntity(wishlist.getId(), wishlist.getMemberId(), wishlist.getProductId());
    }

    public Wishlist toDomain() {
        return Wishlist.of(id, memberId, productId);
    }

}
