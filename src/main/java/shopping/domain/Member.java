package shopping.domain;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(String email, String password) {
        validatedEmail(email);
        validatedPassword(password);
        this.email = email;
        this.password = password;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    private void validatedEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("이메일을 입력해주세요");
        }
    }

    private void validatedPassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8 ~ 20 자리로 입력해주세요");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
