package shopping.member.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import shopping.wish.domain.Wish;

@Document(collection = "members")
public class Member {

    @Id
    private UUID id;

    private String email;
    private String password;
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

    public Wish wish(UUID productId) {
        boolean exists = wishes.stream().anyMatch(w -> w.getProductId().equals(productId));
        if (exists) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }
        Wish wish = new Wish(productId);
        wishes.add(wish);
        return wish;
    }

    public void removeWish(UUID productId) {
        wishes.removeIf(w -> w.getProductId().equals(productId));
    }
}
