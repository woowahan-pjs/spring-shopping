package shopping.wish.application

import org.springframework.stereotype.Service
import shopping.wish.domain.WishProduct

@Service
class WishListQueryService(
    private val wishListQueryRepository: WishListQueryRepository,
) {
    fun findWishList(memberId: Long): Set<WishProduct> {
        val wishList = wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) ?: return setOf()

        return wishList.getWishProducts()
    }
}
