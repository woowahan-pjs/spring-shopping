package shopping.wishlist.application

import java.time.LocalDateTime

data class WishlistProductDto(
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val productImageUrl: String,
    val createdAt: LocalDateTime?,
)
