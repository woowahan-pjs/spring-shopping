package shopping.interfaces.api.wish

import shopping.domain.wish.Wish
import java.time.ZonedDateTime

class WishV1Dto {
    data class AddWishRequest(
        val productId: Long
    )

    data class WishResponse(
        val id: Long,
        val memberId: Long,
        val productId: Long,
        val createdAt: ZonedDateTime
    ) {
        companion object {
            fun from(wish: Wish): WishResponse {
                return WishResponse(
                    id = wish.id,
                    memberId = wish.memberId,
                    productId = wish.productId,
                    createdAt = wish.createdAt
                )
            }
        }
    }
}
