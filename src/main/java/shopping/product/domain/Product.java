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

    private Product(
            ProductName name,
            String description,
            ProductImageUrl imageUrl,
            ProductPrice price,
            Long memberId
    ) {
        this.stockQuantity = 0;
        this.status = ProductStatus.ACTIVE;
        this.createdMemberId = memberId;
        this.updatedMemberId = memberId;
        applyDetails(name, description, imageUrl, price);
    }

    public static Product create(
            ProductName name,
            String description,
            ProductImageUrl imageUrl,
            ProductPrice price,
            Long memberId
    ) {
        return new Product(name, description, imageUrl, price, memberId);
    }

    public void update(
            ProductName name,
            String description,
            ProductImageUrl imageUrl,
            ProductPrice price,
            Long memberId
    ) {
        requireOwner(memberId);
        applyDetails(name, description, imageUrl, price);
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

    private void applyDetails(
            ProductName name,
            String description,
            ProductImageUrl imageUrl,
            ProductPrice price
    ) {
        this.name = name.value();
        this.description = nullIfBlank(description);
        this.imageUrl = imageUrl.value();
        this.price = price.value();
    }

    // 설명이 비어 있으면 의미 없는 값으로 보지 않고 null로 저장한다.
    private String nullIfBlank(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description;
    }

    private void requireOwner(Long memberId) {
        if (createdMemberId.equals(memberId)) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }
}
