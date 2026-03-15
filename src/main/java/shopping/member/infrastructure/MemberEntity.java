package shopping.member.infrastructure;

import jakarta.persistence.*;
import shopping.common.BaseEntity;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRole;

@Entity
@Table(name = "member")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    protected MemberEntity() {}

    public MemberEntity(String email, String password, MemberRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static MemberEntity from(Member member) {
        return new MemberEntity(member.getEmail(), member.getPassword(), member.getRole());
    }

    public Member toDomain() {
        return Member.of(id, email, password, role);
    }
}
