package shopping.member.client.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import shopping.common.domain.BaseEntity;

@Entity
@Table(name = "wish_products")
public class WishProduct extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "wish_product_id", nullable = false)
    private Long id;

    private Long productId;

    protected WishProduct() {
    }

    public WishProduct(final Long productId) {
        this.productId = productId;
    }

    public boolean isSameProduct(WishProduct wishProduct){
        return this.productId.equals(wishProduct.productId);
    }

    public Long getProductId() {
        return productId;
    }
}
