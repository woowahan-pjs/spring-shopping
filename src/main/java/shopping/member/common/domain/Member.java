package shopping.member.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import shopping.common.domain.BaseEntity;

@Entity
@Table(name = "members")
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Member extends BaseEntity {

    @Id
    @Column(name = "email", nullable = false)
    @Getter
    private String email;

    @Embedded
    private Password password;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    protected Member() {
    }

    protected Member(final String email, final Password password, final String memberName) {
        this.email = email;
        this.password = password;
        this.memberName = memberName;
    }

    public boolean isValidPassword(final String rawPassword,
            final PasswordEncoder passwordEncoder) {
        return password.isMatch(rawPassword, passwordEncoder);
    }
}
