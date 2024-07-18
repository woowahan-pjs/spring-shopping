package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import shopping.common.domain.AuditableBaseEntity;

@Entity
public class Product extends AuditableBaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductPrice price;

    @Column(name = "product_image")
    private String image;

    protected Product() {
    }

    public Product(final String name, final ProfanityChecker profanityChecker, final Long price,
            final String image) {
        this.name = new ProductName(name, profanityChecker);
        this.price = new ProductPrice(price);
        this.image = image;
    }
}
