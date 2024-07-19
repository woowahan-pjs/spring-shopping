package shopping.wishlist.dto;

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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModWishList {

        private int cnt;

    }
}
