package shopping.product.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;
import shopping.BaseEntity;
import shopping.constant.enums.YesNo;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prdctSn;

    @Column
    private String prdctId;

    @Embedded
    @Column
    private Name prdctNm;

    @Column
    private BigDecimal price;

    @Column(length = 2000)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private YesNo delYn = YesNo.N;

    public String getPrdctNmValue() {
        return prdctNm.getName();
    }

    public String getDelYnValue() {
        return delYn.name();
    }

    public void updateProductId() {
        this.prdctId = generateProductId(prdctSn);
    }

    private String generateProductId(Long prdctSn) {
        String prefix = "P";
        String formattedNumber = String.format("%04d", prdctSn);
        return prefix + formattedNumber;
    }

    public void updateNameOrImage(String prdctNm, String image, BigDecimal price) {
        updateName(prdctNm);
        updateImage(image);
        updatePrice(price);
    }

    private void updatePrice(BigDecimal price) {
        if(price != null && !this.price.equals(price)) {
            this.price = price;
        }
    }

    private void updateImage(String image) {
        if(isNotBlankValue(image)) {
            this.image = image;
        }
    }

    private void updateName(String prdctNm) {
        if(!prdctNm.isBlank()) {
            this.prdctNm = Name.from(prdctNm);
        }
    }

    private boolean isNotBlankValue(String value) {
        return value != null && !value.isBlank();
    }

    public void updateDelYn(YesNo yesNo) {
        this.delYn = yesNo;
    }

    public boolean eqPrdctNm(String prdctNm) {
        Name productName = Name.from(prdctNm);
        return this.prdctNm.equals(productName);
    }
}
