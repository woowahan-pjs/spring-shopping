package shopping.member.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shopping.BaseEntity;
import shopping.config.utils.EncryptUtils;
import shopping.constant.enums.YesNo;
import shopping.exception.AuthorizationException;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mbrSn;

    @Column
    private String email;

    @Column(name = "password")
    private String encryptedPassword;

    @Column(name = "mbr_nm")
    private String encryptedMbrNm;

    @Column
    private String nickNm;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private YesNo delYn = YesNo.N;


    @Transient
    private String password;

    @Transient
    private String mbrNm;

    public Member(String email, String encryptedPassword) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }



    public String getDelYnStr() {
        return delYn.name();
    }

    public void updatePwdOrName(String password, String name, String nickNm) {
        updatePassword(password);
        updateName(name);
        updateNickNm(nickNm);
    }


    @PrePersist
    @PreUpdate
    public void encryptInfo() {
        this.encryptedMbrNm = encryptValue(this.mbrNm);
        this.encryptedPassword = encryptPassword(this.password);

    }

    @PostLoad
    public void decryptInfo() {
        this.mbrNm = decryptValue(this.encryptedMbrNm);
    }


    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


    private String encryptValue(String value) {
        return value == null || value.isBlank() ? null : EncryptUtils.encrypt(value);
    }

    private String decryptValue(String encryptedValue) {
        return encryptedValue == null || encryptedValue.isBlank() ? null : EncryptUtils.decrypt(encryptedValue);
    }


    private void updateName(String name) {
        if (!name.isBlank()) {
            this.mbrNm = name;
        }
    }

    private void updatePassword(String password) {
        if (!password.isBlank()) {
            this.password = password;
        }
    }

    private void updateNickNm(String nickNm) {
        if (!nickNm.isBlank()) {
            this.nickNm = nickNm;
        }
    }

    public void updateDelYn(YesNo delYn) {
        this.delYn = delYn;
    }

    public void checkPassword(String password) {
        if (isNotEqualPassword(password)) {
            throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isNotEqualPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return !passwordEncoder.matches(password, this.encryptedPassword);
    }
}
