package shopping.wishlist.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import shopping.BaseEntity;
import shopping.product.domain.Product;


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

    @Column(nullable = false, columnDefinition = "int default 1")
    private int cnt;

    @OneToOne
    @JoinColumn(name = "prdctSn")
    private Product product;

    public Long getProductSn() {
        return product.getPrdctSn();
    }
}
