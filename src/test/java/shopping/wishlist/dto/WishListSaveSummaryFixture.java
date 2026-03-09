package shopping.wishlist.dto;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

public class WishListSaveSummaryFixture {

    public static WishListSaveSummary fixture(final int allCount, final int successCount, final int failCount, final List<WishListItemSaveSummary> details) {
        WishListSaveSummary wishListSaveSummary = new WishListSaveSummary();

        ReflectionTestUtils.setField(wishListSaveSummary, "allCount", allCount);
        ReflectionTestUtils.setField(wishListSaveSummary, "successCount", successCount);
        ReflectionTestUtils.setField(wishListSaveSummary, "failCount", failCount);
        ReflectionTestUtils.setField(wishListSaveSummary, "details", details);

        return wishListSaveSummary;
    }
}
