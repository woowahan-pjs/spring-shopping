package shopping.wish.domain;

import jakarta.persistence.*;
import shopping.member.domain.Member;
import shopping.product.domain.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WishList {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "wishList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected WishList() {

    }

    public static WishList create(Long id) {
        return new WishList();
    }

    public Long getId() {
        return id;
    }

    public void addItem(Product product) {
        WishListItem item = WishListItem.create(this, product);
        items.add(item);
    }
}
