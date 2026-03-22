package shopping.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import shopping.wishlist.domain.WishListSaveStatus;

@Schema(name = "[위시 리스트] 위시 리스트 항목 저장 상세 응답 DTO", description = "위시 리스트 저장 상세 결과를 담은 응답 DTO입니다.")
public record WishListItemSaveSummary(
    @Schema(description = "위시 리스트 아이템 저장 결과", example = "SUCCEEDED")
    WishListSaveStatus status,

    @Schema(description = "위시 리스트 아이템 고유 ID", example = "79")
    Long wishId,

    @Schema(description = "상품 고유 ID", example = "703")
    Long productId,

    @Schema(description = "결과 메세지", example = "저장에 성공하였습니다.")
    String message
) {

    public static WishListItemSaveSummary fail(final Long productId, final String message) {
        return new WishListItemSaveSummary(WishListSaveStatus.FAILED, null, productId, message);
    }

    public static WishListItemSaveSummary success(final Long wishId, final Long productId) {
        return new WishListItemSaveSummary(WishListSaveStatus.SUCCEEDED, wishId, productId, "저장에 성공하였습니다.");
    }
}
