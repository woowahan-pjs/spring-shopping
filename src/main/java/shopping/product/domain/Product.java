package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Getter;
import shopping.common.domain.AuditableBaseEntity;

@Entity
public class Product extends AuditableBaseEntity {

    @Id
    @GeneratedValue
    @Getter
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductPrice price;

    @Column(name = "product_image")
    @Getter
    private String image;

    @Column(name = "product_heart_count")
    private Long heartCount;

    protected Product() {
    }

    public Product(final String name, final ProfanityChecker profanityChecker, final Long price,
            final String image) {
        this(null, name, profanityChecker, price, image);
    }

    public Product(final Long id, final String name, final ProfanityChecker profanityChecker,
            final Long price, final String image) {
        this.id = id;
        this.name = new ProductName(name, profanityChecker);
        this.price = new ProductPrice(price);
        this.image = image;
        this.heartCount = 0L;
    }

    public void update(final String name, final ProfanityChecker profanityChecker, final Long price,
            final String image) {
        this.name = new ProductName(name, profanityChecker);
        this.price = new ProductPrice(price);
        this.image = image;
    }

    public void wish() {
        this.heartCount++;
    }

    public void unWish() {
        this.heartCount--;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
