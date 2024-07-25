package shopping.member.domain;


import jakarta.persistence.*;
import lombok.*;
import shopping.BaseEntity;
import shopping.config.utils.EncryptionUtil;
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
    private byte[] encryptedPassword;

    @Column
    private String mbrNm;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private YesNo delYn = YesNo.N;


    @Transient
    private String password;



    public String getDelYnStr() {
        return delYn.name();
    }

    public void updatePwdOrName(String password, String name) {
        updatePassword(password);
        updateName(name);
    }


    @PrePersist
    @PreUpdate
    public void encryptPassword() {
        this.encryptedPassword = encrypt(this.password);
    }

    @PostLoad
    public void decryptPassword() {
        this.password = decrypt(this.encryptedPassword);
    }

    private byte[] encrypt(String password) {
        byte[] encodedValue = EncryptionUtil.encrypt(password);
        return encodedValue;
    }

    private String decrypt(byte[] encryptedPassword) {
        String decryptedPassword = EncryptionUtil.decrypt(encryptedPassword);
        return decryptedPassword;
    }


    private void updateName(String name) {
        if(!name.isBlank()) {
            this.mbrNm = name;
        }
    }

    private void updatePassword(String password) {
        if(!password.isBlank()) {
            this.password = password;
        }
    }

    public void updateDelYn(YesNo delYn) {
        this.delYn = delYn;
    }

    public void checkPassword(String password) {
        if(isNotEqualPassword(password)) {
            throw new AuthorizationException("비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isNotEqualPassword(String password) {
        return !this.password.equals(password);
    }
}
