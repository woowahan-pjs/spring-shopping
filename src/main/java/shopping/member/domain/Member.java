package shopping.member.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import shopping.wish.domain.Wish;

@Entity
@Table(name = "member")
public class Member {

    @Id
    private UUID id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    private List<Wish> wishes = new ArrayList<>();

    protected Member() {}

    public Member(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this(UUID.randomUUID(), email, password);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Wish> getWishes() {
        return Collections.unmodifiableList(wishes);
    }

    public void login(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public Wish wish(UUID productId, long wishedPrice) {
        return wishes.stream().filter(w -> w.getProductId().equals(productId)).findFirst()
                .orElseGet(() -> {
                    Wish wish = new Wish(this.id, productId, wishedPrice);
                    wishes.add(wish);
                    return wish;
                });
    }

    public void removeWish(UUID productId) {
        wishes.removeIf(w -> w.getProductId().equals(productId));
    }
}
