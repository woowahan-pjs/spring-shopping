package shopping.wish.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "wish",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"}))
public class Wish {

    @Id
    private UUID id;

    @Column(name = "member_id", insertable = false, updatable = false)
    private UUID memberId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "wished_price", nullable = false)
    private long wishedPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Wish() {}

    public Wish(UUID memberId, UUID productId, long wishedPrice) {
        this.id = UUID.randomUUID();
        this.memberId = memberId;
        this.productId = productId;
        this.wishedPrice = wishedPrice;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public UUID getProductId() {
        return productId;
    }

    public long getWishedPrice() {
        return wishedPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
