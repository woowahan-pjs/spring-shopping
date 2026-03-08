package shopping.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shopping.wish.Wish;

public class Member {

    private Long id;
    private String email;
    private String password;
    private List<Wish> wishes = new ArrayList<>();

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this(null, email, password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Wish> getWishes() {
        return Collections.unmodifiableList(wishes);
    }

    public Member withId(Long id) {
        Member member = new Member(id, this.email, this.password);
        member.wishes = this.wishes;
        return member;
    }

    public void login(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    public Wish wish(Long productId) {
        boolean exists = wishes.stream().anyMatch(w -> w.getProductId().equals(productId));
        if (exists) {
            throw new IllegalArgumentException("이미 위시리스트에 추가된 상품입니다.");
        }
        Wish wish = new Wish(productId);
        wishes.add(wish);
        return wish;
    }

    public void removeWish(Long productId) {
        wishes.removeIf(w -> w.getProductId().equals(productId));
    }
}
