package shopping.member.domain;

import jakarta.persistence.*;
import shopping.wish.domain.WishList;

import java.time.LocalDateTime;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private WishList wishList;

    protected Member() {
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

    private Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Member create(String email, String password) {
        Member member = new Member(email, password);
        WishList.create(member);
        return member;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }
}
