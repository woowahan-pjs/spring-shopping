package shopping.wishlist.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.wishlist.domain.WishList;

@Schema(name = "[위시 리스트] 위시 리스트 조회 응답 DTO", description = "위시 리스트를 담은 DTO 입니다.")
public record WishListResponse(
        @Schema(description = "회원 고유 ID", example = "507") Long userId,
        @Schema(description = "위시 리스트 상품") List<WishListItemResponse> items) {

    public static WishListResponse from(final Long userId, final WishList wishList) {
        return new WishListResponse(
                userId, wishList.getItems().stream().map(WishListItemResponse::from).toList());
    }
}
