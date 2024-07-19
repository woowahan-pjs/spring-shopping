package shopping.products.domain;


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
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prdctSn;

    @Column(length = 20, nullable = false)
    private String prdctId;

    @Column(length = 45, nullable = false)
    private String prdctNm;

    @Column(length = 2000)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('Y', 'N')")
    private YesNo delYn;
}
