package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import shopping.common.domain.BaseEntity;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private BigDecimal price;

    @Column(name = "product_image", nullable = false)
    private String image;

}
