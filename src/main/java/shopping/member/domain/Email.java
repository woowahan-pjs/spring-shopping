package shopping.member.domain;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private String email;

    protected Email() {
    }

    private Email(String email) {
        this.email = email;
    }

    public static Email from(String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("올바른 이메일 주소 양식이 아닙니다.");
        }

        return new Email(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(this.email, email.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
