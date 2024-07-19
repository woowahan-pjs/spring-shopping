package shopping.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.member.domain.Member;
import shopping.wishlist.domain.WishList;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WishListResponse {

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class WishLists {
//        private int totalCnt;
//        private List<WishWithProductDetail> wishLists;

//        public static Members from(List<Member> members) {
//            return Members.builder()
//                    .totalCnt(members.size())
//                    .members(addMembers(members))
//                    .build();
//        }
//
//        private static List<MemberDetail> addMembers(List<Member> members) {
//            return members.stream()
//                    .map(MemberDetail::from)
//                    .toList();
//        }
//    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishDetail {

        private Long wishSn;
        private Long mbrSn;
        private Long prdctSn;
        private Long cnt;

        public static WishDetail from(WishList wishList) {
            return WishDetail.builder()
                    .wishSn(wishList.getWishSn())
                    .mbrSn(wishList.getMbrSn())
                    .prdctSn(wishList.getProductSn())
                    .cnt(wishList.getCnt())
                    .build();
        }
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class WishWithProductDetail {

//        private Long mbrSn;
//        private String email;
//        private String name;
//        private String delYn;
//        private LocalDateTime regDt;
//
//        public static MemberDetail from(Member persistMember) {
//            return MemberDetail.builder()
//                    .mbrSn(persistMember.getMbrSn())
//                    .email(persistMember.getEmail())
//                    .name(persistMember.getMbrNm())
//                    .delYn(persistMember.getDelYnStr())
//                    .regDt(persistMember.getRegDt())
//                    .build();
//        }
//    }
}
