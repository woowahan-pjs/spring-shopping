package shopping.application.wish

import shopping.domain.wish.Wish
import shopping.interfaces.api.wish.WishV1Dto

class WishFacade(
    private val wishService: WishService
) {
    fun addWish(memberId: Long, request: WishV1Dto.AddWishRequest): Wish {
        return wishService.addWish(memberId, request.productId)
    }

    fun removeWish(memberId: Long, wishId: Long) {
        wishService.removeWish(memberId, wishId)
    }

    fun getWishes(memberId: Long): List<Wish> {
        return wishService.getWishes(memberId)
    }
}
