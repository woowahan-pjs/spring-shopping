package shopping.member.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Member {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Embedded
    private Password password;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_priority", nullable = false)
    private MemberPriority memberPriority;

    protected Member() {
    }

    protected Member(final String email, final Password password, final String memberName,
        final MemberPriority memberPriority) {
        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.memberPriority = memberPriority;
    }
}
