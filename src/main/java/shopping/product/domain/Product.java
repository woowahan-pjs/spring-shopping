package shopping.product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Column(name = "email", nullable = false, columnDefinition = "varchar(15)")
    private String name;

    @NotNull
    @Comment("가격")
    @Column(name = "price", nullable = false, columnDefinition = "bigint")
    private BigDecimal price;

    @NotNull
    @Comment("이미지 URL")
    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(255)")
    private String imageUrl;

    @NotNull
    @Comment("사용여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private Boolean isUse;
}
