package shopping.domain.wishlist;

import jakarta.persistence.*;
import shopping.domain.member.Member;
import shopping.domain.product.Product;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wishlist() {

    }

    public Wishlist(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public static Wishlist create(Member member, Product product) {
        return new Wishlist(member, product);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
}
