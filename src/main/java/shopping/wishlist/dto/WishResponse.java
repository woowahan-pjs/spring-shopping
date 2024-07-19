package shopping.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.wishlist.domain.Wish;
import shopping.wishlist.domain.WishList;

import java.util.List;

@Data
public class WishResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishListRes {
        private int totalCnt;
        private List<WishWithProductDetail> wishList;

        public static WishListRes from(WishList wishList) {
            return WishListRes.builder()
                    .totalCnt(wishList.getTotCnt())
                    .wishList(addWishList(wishList.getWishList()))
                    .build();
        }

        private static List<WishWithProductDetail> addWishList(List<Wish> wishList) {
            return wishList.stream()
                    .map(WishWithProductDetail::from)
                    .toList();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishDetail {

        private Long wishSn;
        private Long mbrSn;
        private Long prdctSn;
        private Long cnt;

        public static WishDetail from(Wish wish) {
            return WishDetail.builder()
                    .wishSn(wish.getWishSn())
                    .mbrSn(wish.getMbrSn())
                    .prdctSn(wish.getProductSn())
                    .cnt(wish.getCnt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishWithProductDetail {
        private Long wishSn;
        private Long mbrSn;
        private Long prdctSn;
        private String prdctId;
        private String prdctNm;
        private String image;
        public static WishWithProductDetail from(Wish persistWish) {
            return WishWithProductDetail.builder()
                    .wishSn(persistWish.getWishSn())
                    .mbrSn(persistWish.getMbrSn())
                    .prdctSn(persistWish.getProductSn())
                    .prdctId(persistWish.getPrdctId())
                    .prdctNm(persistWish.getPrdctNm())
                    .image(persistWish.getPrdctImage())
                    .build();
        }
    }
}
