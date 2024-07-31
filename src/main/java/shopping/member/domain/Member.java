package shopping.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.StringUtils;
import shopping.member.exception.InvalidMemberException;

import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class Member {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    public Member() {
    }

    public Member(final String email, final String password) {
        validate(email, password);
        this.email = email;
        this.password = password;
    }

    private void validate(final String email, final String password) {
        if (!StringUtils.hasText(email)) {
            throw new InvalidMemberException("회원 이메일은 필수값 입니다.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidMemberException("유효하지 않은 이메일 입니다.");
        }

        if (!StringUtils.hasText(password)) {
            throw new InvalidMemberException("회원 비밀번호는 필수값 입니다.");
        }
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

    public boolean checkPassword(final String password) {
        return Objects.equals(this.password, password);
    }
}
