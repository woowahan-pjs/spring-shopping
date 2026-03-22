package shopping.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "[위시 리스트] 위시 리스트 저장 요청 DTO", description = "위시 리스트 저장 요청을 위한 DTO 입니다.")
public record WishListSaveRequest(

    @Valid
    @NotEmpty(message = "위시리스트 상품은 1개 이상이어야 합니다.")
    @Size(max = 10, message = "위시리스트 상품은 최대 10개까지 등록할 수 있습니다.")
    List<WishListItemSaveRequest> items
) {

    public List<Long> extractIds() {
        return items.stream()
                .map(WishListItemSaveRequest::productId)
                .toList();
    }
}
