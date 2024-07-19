package shopping.wishlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.product.domain.Product;
import shopping.wishlist.domain.Wish;

@Data
public class WishRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegWishList {

        private Long mbrSn;

        private Long prdctSn;


        public Wish toWishList(Product product) {
            return Wish.builder()
                    .mbrSn(mbrSn)
                    .product(product)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModWishProductCnt {
        @Min(1)
        @Builder.Default
        private Long cnt = 1L;

        @JsonProperty("isAdd")
        @Builder.Default
        private boolean isAdd = true;
    }
}
