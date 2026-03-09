package shopping.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.dto.WishListResponse;
import shopping.wishlist.repository.WishListRepository;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;

    /**
     * 특정 회원의 위시 리스트를 조회하여 반환합니다. 위시 리스트가 존재하지 않을 경우 빈 리스트가 포함된 객체를 생성하여 반환합니다.
     *
     * @param userId 위시 리스트를 조회할 대상 회원의 고유 ID입니다.
     * @return 조회된 위시 리스트 데이터를 담고 있는 WishListResponse 객체를 반환합니다.
     */
    @Transactional(readOnly = true)
    public WishListResponse getWishList(final Long userId) {
        final WishList wishList = wishListRepository.findByWishListAndIsUse(userId).orElse(WishList.generate(userId));

        return WishListResponse.from(userId, wishList);
    }

    /**
     * 특정 회원의 위시 리스트에서 지정된 항목을 비활성화합니다. 항목이 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param userId 위시 리스트를 소유한 회원의 고유 ID입니다.
     * @param wishListItemId 비활성화할 WishListItem의 고유 ID입니다.
     */
    @Transactional
    public void removeWishList(final Long userId, final Long wishListItemId) {
        final WishList wishList = wishListRepository.findByWishList(userId)
                        .orElseThrow(() -> new ShoppingBusinessException("등록된 위시 리스트가 존재하지 않습니다."));

        wishList.removeItem(wishListItemId);
    }
}
