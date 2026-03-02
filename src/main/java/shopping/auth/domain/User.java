package shopping.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import shopping.infra.orm.AuditInformation;
import shopping.infra.orm.BooleanYnConverter;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Comment("이메일")
    @Column(name = "email", unique = true, nullable = false, columnDefinition = "varchar(320)")
    private String email;

    @NotNull
    @Comment("비밀번호")
    @Column(name = "password", nullable = false, columnDefinition = "varchar(60)")
    private String password;

    @NotNull
    @Comment("권한")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(16)")
    private Role role;

    @NotNull
    @Comment("사용여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private Boolean isUse;
}
