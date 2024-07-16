package shopping.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    private String password;
    @CreatedDate
    private LocalDateTime registeredAt;

    protected Member() {
    }

    Member(Email email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Member from(MemberCreate memberCreate) {
        return new Member(Email.from(memberCreate.email()), memberCreate.password());
    }

    public boolean hasEqualEmail(Email email) {
        return this.email.equals(email);
    }

    public Long getId() {
        return id;
    }
}
