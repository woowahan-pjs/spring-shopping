package shopping.wish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    private WishlistItem(Wishlist wishlist, Long productId, Integer quantity) {
        this.wishlist = wishlist;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static WishlistItem create(Wishlist wishlist, Long productId, Integer quantity) {
        return new WishlistItem(wishlist, productId, quantity);
    }

    @PrePersist
    protected void prePersist() {
        addedAt = LocalDateTime.now();
    }
}
