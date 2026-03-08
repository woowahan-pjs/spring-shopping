package shopping.wishlist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
        final WishList wishList =
                wishListRepository.findByWishList(userId).orElse(WishList.generate());

        return WishListResponse.from(userId, wishList);
    }
}
