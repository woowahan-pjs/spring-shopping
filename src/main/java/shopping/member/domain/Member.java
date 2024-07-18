package shopping.member.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import shopping.BaseEntity;
import shopping.constant.enums.YesNo;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mbrNo;
    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn", nullable = false, columnDefinition = "ENUM('Y', 'N')")
    private YesNo delYn;
}
