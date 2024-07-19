package shopping.member.domain;


import jakarta.persistence.*;
import lombok.*;
import shopping.BaseEntity;
import shopping.constant.enums.YesNo;


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

    @Column
    private String password;

    @Column
    private String mbrNm;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private YesNo delYn = YesNo.N;

    public String getDelYnStr() {
        return delYn.name();
    }

    public void updatePwdOrName(String password, String name) {
        updatePassword(password);
        updateName(name);
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
}
