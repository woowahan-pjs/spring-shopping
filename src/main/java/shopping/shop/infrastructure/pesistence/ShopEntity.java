package shopping.shop.infrastructure.pesistence;

import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShopEntity that = (ShopEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
