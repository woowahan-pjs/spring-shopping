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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public static WishList create(Member member) {
        WishList wishList = new WishList();
        wishList.setMember(member);
        return wishList;
    }

    public void addItem(Product product) {
        WishListItem item = WishListItem.create(this, product);
        items.add(item);
    }

    public void setMember(Member member) {
        this.member = member;

        if (member.getWishList() != this) {
            member.setWishList(this);
        }
    }
}
