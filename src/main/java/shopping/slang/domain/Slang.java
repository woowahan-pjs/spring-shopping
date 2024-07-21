package shopping.slang.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import shopping.BaseEntity;
import shopping.constant.enums.YesNo;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Slang  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slangSn;

    @Column
    private String slang;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private YesNo purgoMalumYn = YesNo.N;


    public boolean hasSlang(String value) {
        return value.contains(slang);
    }
}
