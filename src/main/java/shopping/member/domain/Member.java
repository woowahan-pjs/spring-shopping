package shopping.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@EntityListeners(AuditingEntityListener.class)
@Table
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @Column(unique = true)
    private Email email;
    private String password;
    @CreatedDate
    private LocalDateTime registeredAt;

    protected Member() {
    }

    private Member(Email email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Member of(MemberCreate memberCreate, PasswordEncoder passwordEncoder) {
        return new Member(Email.from(memberCreate.email()), passwordEncoder.encode(memberCreate.password()));
    }

    public boolean hasEqualEmail(Email email) {
        return this.email.equals(email);
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
