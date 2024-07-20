package shopping.wishlist.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shopping.member.domain.Member;
import shopping.product.domain.Product;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class WishProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    protected WishProduct() {
    }

    private WishProduct(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public static WishProduct of(Member member, Product product) {
        return new WishProduct(member, product);
    }
}
