package shopping.wishlist.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.repository.ProductRepository;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListItem;
import shopping.wishlist.domain.WishListSaveContext;
import shopping.wishlist.dto.WishListItemSaveSummary;
import shopping.wishlist.dto.WishListSaveRequest;
import shopping.wishlist.dto.WishListSaveSummary;
import shopping.wishlist.repository.WishListItemRepository;
import shopping.wishlist.repository.WishListRepository;

@Service
@RequiredArgsConstructor
public class WishListSaveService {

    private final WishListRepository wishListRepository;

    private final WishListItemRepository wishListItemRepository;

    private final ProductRepository productRepository;

    /**
     * 특정 회원의 위시 리스트에 새로운 항목을 추가하거나 기존 위시 리스트를 갱신합니다.
     * 요청된 항목 중 등록 가능한 항목만 추가되고, 결과 요약 정보를 반환합니다.
     *
     * @param userId 위시 리스트를 소유한 회원의 고유 ID입니다.
     * @param request 위시 리스트에 추가할 항목의 정보를 담고 있는 WishListSaveRequest 객체입니다.
     * @return 위시 리스트 항목 추가 작업의 성공 및 실패 내역을 포함한 WishListSaveSummary 객체를 반환합니다.
     */
    @Transactional
    public WishListSaveSummary registerWishList(final Long userId, final WishListSaveRequest request) {
        final WishList wishList =
            wishListRepository.findByWishList(userId)
                .orElseGet(() -> wishListRepository.save(WishList.generate(userId)));

        final WishListSaveContext context = createSaveContext(wishList, request.extractIds());

        for (Long it : context.getRequestProductIds()) {
            context.addSummary(processAndAppendResult(context, it));
        }

        return context.summary();
    }

    /**
     * 위시 리스트에 새로운 항목을 저장하고 결과 요약 정보를 생성합니다.
     * 주어진 항목이 이미 등록되어 있거나 유효하지 않은 경우 실패 결과를 반환합니다.
     *
     * @param context 위시 리스트 저장 상태 및 정보를 포함하는 WishListSaveContext 객체입니다.
     * @param productId 저장하려는 상품의 고유 ID입니다.
     * @return 저장 작업의 성공 여부 및 세부 내용을 담은 WishListItemSaveSummary 객체를 반환합니다.
     */
    private WishListItemSaveSummary processAndAppendResult(final WishListSaveContext context, final Long productId) {
        if (context.isContainsItem(productId)) {
            return WishListItemSaveSummary.fail(productId, "이미 등록된 항목입니다.");
        }

        if (context.isNotContainsProductId(productId)) {
            return WishListItemSaveSummary.fail(productId, "존재하지 않는 상품입니다.");
        }

        final WishListItem item = wishListItemRepository.save(WishListItem.generate(context.getWishListId(), context.getValidProduct(productId)));

        return WishListItemSaveSummary.success(item.getWishListId(), productId);
    }

    /**
     * 주어진 WishList와 요청된 상품 ID 목록을 기반으로 WishListSaveContext를 생성합니다.
     *
     * @param wishList 위시 리스트 정보를 담고 있는 WishList 객체입니다.
     * @param requestProductIds 요청된 상품의 고유 ID 목록입니다.
     * @return 생성된 WishListSaveContext 객체를 반환합니다.
     */
    private WishListSaveContext createSaveContext(final WishList wishList, final List<Long> requestProductIds) {
        return WishListSaveContext.create(wishList, requestProductIds, productRepository.findByIsUseAndIdIn(true, requestProductIds));
    }
}
