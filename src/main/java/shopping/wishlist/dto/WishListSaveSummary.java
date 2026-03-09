package shopping.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "[위시 리스트] 위시 리스트 항목 저장 응답 DTO", description = "위시 리스트 저장 결과를 담은 응답 DTO입니다.")
public class WishListSaveSummary {

    @Schema(description = "전체 요청 건 수", example = "5")
    private int allCount;

    @Schema(description = "저장 성공 건 수", example = "3")
    private int successCount;

    @Schema(description = "저장 실패 건 수 (이미 등록 혹은 존재하지 않는 상품의 경우)", example = "2")
    private int failCount;

    private List<WishListItemSaveSummary> details;

    public static WishListSaveSummary create(final int allCount) {
        WishListSaveSummary summary = new WishListSaveSummary();

        summary.allCount = allCount;
        summary.successCount = 0;
        summary.failCount = 0;
        summary.details = new ArrayList<>();

        return summary;
    }

    public void add(final WishListItemSaveSummary wishListItemSaveSummary) {
        details.add(wishListItemSaveSummary);

        if (ObjectUtils.isEmpty(wishListItemSaveSummary.wishId())) {
            failCount++;

            return;
        }

        successCount++;
    }
}
