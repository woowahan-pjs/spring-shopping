package shopping.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "[위시 리스트] 위시 리스트 항목 저장 요청 DTO", description = "위시 리스트에 담고 싶은 상품 ID를 담은 DTO 입니다.")
public record WishListItemSaveRequest(

    @NotNull(message = "상품 고유 ID는 필수입니다.")
    @Schema(description = "상품 고유 ID", example = "703")
    Long productId
) {

}
