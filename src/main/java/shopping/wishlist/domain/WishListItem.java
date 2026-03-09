package shopping.wishlist.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.infra.orm.AuditInformation;
import shopping.infra.orm.BooleanYnConverter;
import shopping.product.domain.Product;

@Getter
@Entity
@Table(name = "wishlist_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishListItem extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Comment("위시 리스트 고유 ID")
    @Column(name = "wishlist_id", nullable = false, columnDefinition = "bigint")
    private Long wishListId;

    @Comment("상품 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Comment("사용여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private Boolean isUse;

    public static WishListItem generate(final Long wishListId, final Product product) {
        WishListItem wishListItem = new WishListItem();

        wishListItem.wishListId = wishListId;
        wishListItem.product = product;
        wishListItem.isUse = true;

        return wishListItem;
    }

    public void remove() {
        this.isUse = false;
    }
}
