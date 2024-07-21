package shopping.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.product.domain.Products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductsRes {
        private int totalCnt;
        private List<ProductDetail> products;

        public static ProductsRes from(Products products) {
            return ProductsRes.builder()
                    .totalCnt(products.getTotCnt())
                    .products(addProducts(products.getProducts()))
                    .build();
        }

        private static List<ProductDetail> addProducts(List<Product> products) {
            return products.stream()
                    .map(ProductDetail::from)
                    .toList();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetail {

        private Long prdctSn;
        private String prdctId;
        private String prdctNm;
        private String image;
        private BigDecimal price;
        private String delYn;
        private LocalDateTime regDt;

        public static ProductDetail from(Product persistProduct) {
            return ProductDetail.builder()
                    .prdctSn(persistProduct.getPrdctSn())
                    .prdctId(persistProduct.getPrdctId())
                    .prdctNm(persistProduct.getPrdctNmValue())
                    .price(persistProduct.getPrice())
                    .image(persistProduct.getImage())
                    .delYn(persistProduct.getDelYnValue())
                    .regDt(persistProduct.getRegDt())
                    .build();
        }
    }
}
