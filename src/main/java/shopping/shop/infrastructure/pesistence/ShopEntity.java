package shopping.shop.infrastructure.pesistence;

import jakarta.persistence.*;

@Table(name = "shops")
@Entity
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long sellerId;

    private String name;

    public ShopEntity() {
    }

    public ShopEntity(final Long id, final long sellerId, final String name) {
        this.id = id;
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
