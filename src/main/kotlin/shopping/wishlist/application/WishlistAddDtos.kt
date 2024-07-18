package shopping.wishlist.application

import shopping.wishlist.domain.WishlistProduct
import java.time.LocalDateTime

data class AddWishlistRequest(
    val productId: Long,
)

data class AddWishlistResponse(
    val productId: Long,
    val createAt: LocalDateTime?,
) {
    constructor(wishlistProduct: WishlistProduct) : this(
        productId = wishlistProduct.id.productId,
        createAt = wishlistProduct.createdAt,
    )
}
