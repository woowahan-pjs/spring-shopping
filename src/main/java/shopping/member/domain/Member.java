package shopping.member.domain;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private MemberRole role;

    public Member(String email, String password) {
        validatedEmail(email);
        validatedPassword(password);
        this.email = email;
        this.password = password;
        this.role = MemberRole.USER;
    }

    private Member(Long id, String email, String password, MemberRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(Long id, String email, String password, MemberRole role) {
        return new Member(id, email, password, role);
    }

    void assignId(Long id) {
        this.id = id;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeRole(MemberRole role) {
        this.role = role;
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

    public MemberRole getRole() {
        return role;
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
        return Objects.equals(id, member.id) && Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
