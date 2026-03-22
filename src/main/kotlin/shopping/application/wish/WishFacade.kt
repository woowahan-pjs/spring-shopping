package shopping.application.wish

import org.springframework.stereotype.Service
import shopping.domain.wish.Wish
import shopping.interfaces.api.wish.WishV1Dto

@Service
class WishFacade(
    private val wishService: WishService,
) {
    fun addWish(
        memberId: Long,
        request: WishV1Dto.AddWishRequest,
    ): Wish = wishService.addWish(memberId, request.productId)

    fun removeWish(
        memberId: Long,
        wishId: Long,
    ) {
        wishService.removeWish(memberId, wishId)
    }

    fun getWishes(memberId: Long): List<Wish> = wishService.getWishes(memberId)
}
