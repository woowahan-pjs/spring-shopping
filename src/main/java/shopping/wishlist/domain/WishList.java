package shopping.wishlist.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import shopping.BaseEntity;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WishList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishSn;

    @Column(nullable = false)
    private Long mbrSn;

    @Column(nullable = false)
    private Long prdctSn;

    @Column(nullable = false, columnDefinition = "int default 1")
    private int cnt;
}
