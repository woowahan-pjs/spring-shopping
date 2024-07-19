package shopping.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.wishlist.domain.Wish;

import java.util.List;

@Data
public class WishResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishList {
        private int totalCnt;
        private List<WishWithProductDetail> wishList;

        public static WishList from(List<Wish> wishList) {
            return WishList.builder()
                    .totalCnt(wishList.size())
                    .wishList(addWishList(wishList))
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

        public static WishDetail from(Wish wishList) {
            return WishDetail.builder()
                    .wishSn(wishList.getWishSn())
                    .mbrSn(wishList.getMbrSn())
                    .prdctSn(wishList.getProductSn())
                    .cnt(wishList.getCnt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishWithProductDetail {

        private Long mbrSn;
        private Long prdctSn;
        private String prdctId;
        private String prdctNm;
        private String image;
        public static WishWithProductDetail from(Wish persistWish) {
            return WishWithProductDetail.builder()
                    .mbrSn(persistWish.getMbrSn())
                    .prdctSn(persistWish.getProductSn())
                    .prdctId(persistWish.getPrdctId())
                    .prdctNm(persistWish.getPrdctNm())
                    .image(persistWish.getPrdctImage())
                    .build();
        }
    }
}
