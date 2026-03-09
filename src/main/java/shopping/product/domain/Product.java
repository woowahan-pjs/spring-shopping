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
import shopping.common.ApiException;
import shopping.common.ErrorCode;
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

    private Product(ProductDetails details, Long memberId) {
        this.stockQuantity = 0;
        this.status = ProductStatus.ACTIVE;
        this.createdMemberId = memberId;
        this.updatedMemberId = memberId;
        applyDetails(details);
    }

    public static Product create(ProductDetails details, Long memberId) {
        return new Product(details, memberId);
    }

    public void update(ProductDetails details, Long memberId) {
        requireOwner(memberId);
        applyDetails(details);
        this.updatedMemberId = memberId;
    }

    public void delete(Long memberId) {
        requireOwner(memberId);
        this.status = ProductStatus.DELETED;
        this.deletedMemberId = memberId;
        markDeleted();
    }

    public boolean isActive() {
        return status == ProductStatus.ACTIVE;
    }

    private void applyDetails(ProductDetails details) {
        this.name = details.name().value();
        this.description = details.description();
        this.imageUrl = details.imageUrl().value();
        this.price = details.price().value();
    }

    private void requireOwner(Long memberId) {
        if (createdMemberId.equals(memberId)) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }
}
