package shopping.product.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shopping.infra.orm.AuditInformation;
import shopping.infra.orm.BooleanYnConverter;
import shopping.product.dto.ProductSaveRequest;
import shopping.product.dto.ProductUpdateRequest;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Comment("상품명")
    @Column(name = "name", nullable = false, columnDefinition = "varchar(15)")
    private String name;

    @NotNull
    @Embedded
    @Comment("가격")
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Price price;

    @NotNull
    @Comment("이미지 URL")
    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(255)")
    private String imageUrl;

    @NotNull
    @Comment("사용여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private Boolean isUse;

    private Long userId;

    public static Product of(final Long userId, final ProductSaveRequest request) {
        Product product = new Product();

        product.name = request.name();
        product.price = request.price();
        product.imageUrl = request.imageUrl();
        product.isUse = true;
        product.userId = userId;

        return product;
    }

    public boolean isEqualsNameTo(final String name) {
        return this.name.equals(name);
    }

    public void modify(final ProductUpdateRequest request) {
        if (this.isEqualsNameTo(request.name())) {
            this.name = request.name();
        }

        if (this.isEqualsPriceTo(request.price())) {
            this.price = request.price();
        }

        if (this.isEqualsImageUrl(request.imageUrl())) {
            this.imageUrl = request.imageUrl();
        }
    }

    public void remove() {
        this.isUse = false;
    }

    private boolean isEqualsPriceTo(final Price price) {
        return this.price.equals(price);
    }

    private boolean isEqualsImageUrl(final String imageUrl) {
        return this.imageUrl.equals(imageUrl);
    }
}
