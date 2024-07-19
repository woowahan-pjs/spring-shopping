package shopping.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.exception.BadRequestException;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.wishlist.domain.WishList;

@Data
public class WishListRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegWishList {


        private Long mbrSn;


        private Long prdctSn;


        public WishList toWishList(Product product) {
            return WishList.builder()
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
