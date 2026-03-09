package shopping.wish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.common.domain.BaseDateEntity;

@Entity
@Table(name = "wishlists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WishlistStatus status;

    private Wishlist(Long memberId) {
        this.memberId = memberId;
        this.status = WishlistStatus.ACTIVE;
    }

    public static Wishlist create(Long memberId) {
        return new Wishlist(memberId);
    }

    public void delete() {
        this.status = WishlistStatus.DELETED;
        markDeleted();
    }

    public boolean isActive() {
        return status == WishlistStatus.ACTIVE;
    }
}
