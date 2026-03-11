package shopping.api.presentation.v1.dto

import shopping.application.dto.CreateWishCommand
import shopping.application.dto.WishResult

data class CreateWishRequest(
    val productId: Long,
) {
    fun toCommand(memberId: Long) = CreateWishCommand(memberId, productId)
}

data class WishResponse(
    val id: Long,
    val memberId: Long,
    val productId: Long,
) {
    companion object {
        fun from(result: WishResult) =
            WishResponse(
                id = result.id,
                memberId = result.memberId,
                productId = result.productId,
            )
    }
}
