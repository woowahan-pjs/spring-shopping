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
public class Wish extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishSn;

    @Column(nullable = false)
    private Long mbrSn;

    @Column(nullable = false, columnDefinition = "bigint default 1")
    @Builder.Default
    private Long cnt = 1L;

    @OneToOne
    @JoinColumn(name = "prdctSn")
    private Product product;

    public Long getProductSn() {
        return product.getPrdctSn();
    }

    public String getPrdctId() {
        return product.getPrdctId();
    }

    public String getPrdctNm() {
        return product.getPrdctNmValue();
    }

    public String getPrdctImage() {
        return product.getImage();
    }
}
