package shopping.member.infrastructure;

import jakarta.persistence.*;
import shopping.common.BaseEntity;
import shopping.member.domain.Member;

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

    protected MemberEntity() {}

    public MemberEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberEntity from(Member member) {
        return new MemberEntity(member.getEmail(), member.getPassword());
    }

    public Member toDomain() {
        return Member.of(id, email, password);
    }
}
