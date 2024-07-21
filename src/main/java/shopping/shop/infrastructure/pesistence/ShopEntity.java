package shopping.shop.infrastructure.pesistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long sellerId;

    private String name;

    public ShopEntity() {
    }

    public ShopEntity(final Long sellerId, final String name) {
        this.sellerId = sellerId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public long getSellerId() {
        return sellerId;
    }

    public String getName() {
        return name;
    }
}
