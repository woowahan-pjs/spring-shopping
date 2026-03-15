package shopping.application.dto

import shopping.domain.Wish

data class CreateWishCommand(
    val memberId: Long,
    val productId: Long,
)

data class WishResult(
    val id: Long,
    val memberId: Long,
    val productId: Long,
) {
    companion object {
        fun from(wish: Wish): WishResult =
            WishResult(
                id = wish.id,
                memberId = wish.memberId,
                productId = wish.productId,
            )
    }
}
