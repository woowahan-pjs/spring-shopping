package shopping.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shopping.exception.BadRequestException;
import shopping.member.domain.Member;
import shopping.product.domain.Name;
import shopping.product.domain.Product;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegProduct {

        @NotBlank
//        @Length(max = 15, message = "상품명은 15자리를 이하여야 합니다.")
        private String prdctNm;

        private String image;

        @DecimalMin("0")
        private BigDecimal price;

        public static RegProduct of(String name, BigDecimal price) {
            return RegProduct.builder()
                    .prdctNm(name)
                    .price(price)
                    .build();
        }

        public static RegProduct of(String name, BigDecimal price, String image) {
            return RegProduct.builder()
                    .prdctNm(name)
                    .price(price)
                    .image(image)
                    .build();
        }

        public Product toProduct() {
            return Product.builder()
                    .prdctNm(Name.from(prdctNm))
                    .price(price)
                    .image(image)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ModProduct {
        private String prdctNm;

        private String image;

        private BigDecimal price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductNameCheck {
        private String prdctNm;
    }
}
