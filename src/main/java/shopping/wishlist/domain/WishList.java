package shopping.wishlist.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.infra.orm.AuditInformation;

@Getter
@Entity
@Table(name = "wishlist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishList extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Comment("회원 고유 ID")
    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long userId;

    @OneToMany(mappedBy = "wishListId", cascade = CascadeType.PERSIST)
    private List<WishListItem> items = new ArrayList<>();

    public static WishList generate(final Long userId) {
        WishList wishList = new WishList();

        wishList.userId = userId;

        return wishList;
    }

    /**
     * 지정된 wishListItemId를 사용하여 위시 리스트에서 해당 항목을 비활성화합니다. 항목이 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param wishListItemId 비활성화할 WishListItem의 고유 ID입니다.
     */
    public void removeItem(final Long wishListItemId) {
        final WishListItem item =
                items.stream()
                        .filter(it -> it.getId().equals(wishListItemId))
                        .findFirst()
                        .orElseThrow(() -> new ShoppingBusinessException("존재하지 않는 위시 리스트 입니다."));

        item.remove();
    }

    public boolean isContainsItem(final Long productId) {
        return items.stream()
            .filter(it -> Boolean.TRUE.equals(it.getIsUse()))
            .anyMatch(it -> it.getProduct().getId().equals(productId));
    }

    public void addItem(final WishListItem item) {
        this.items.add(item);
    }
}
