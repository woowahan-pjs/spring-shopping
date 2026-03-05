package shopping.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.common.domain.BaseDateEntity;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(nullable = false)
    private Long createdMemberId;

    private Long updatedMemberId;

    private Long deletedMemberId;

    private Product(
            String name,
            String description,
            String imageUrl,
            BigDecimal price,
            Long memberId
    ) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stockQuantity = 0;
        this.status = ProductStatus.ACTIVE;
        this.createdMemberId = memberId;
        this.updatedMemberId = memberId;
    }

    public static Product create(
            String name,
            String description,
            String imageUrl,
            BigDecimal price,
            Long memberId
    ) {
        return new Product(name, description, imageUrl, price, memberId);
    }

    public void update(String name, String description, String imageUrl, BigDecimal price, Long memberId) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.updatedMemberId = memberId;
    }

    public void delete(Long memberId) {
        this.status = ProductStatus.DELETED;
        this.deletedMemberId = memberId;
        markDeleted();
    }

    public boolean isActive() {
        return status == ProductStatus.ACTIVE;
    }
}
