package shopping.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.common.domain.BaseDateEntity;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    private Member(String email, String password, MemberStatus status, MemberRole role) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public static Member create(
            String email,
            String password,
            MemberStatus status,
            MemberRole role
    ) {
        return new Member(email, password, status, role);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void deactivate() {
        this.status = MemberStatus.INACTIVE;
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

    public boolean isSeller() {
        return role == MemberRole.SELLER;
    }
}
