package shopping.wishlist.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import shopping.common.domain.CurrentMember
import shopping.product.application.ProductNotFoundException
import shopping.product.domain.ProductRepository
import shopping.wishlist.domain.WishlistProduct
import shopping.wishlist.domain.WishlistProductId
import shopping.wishlist.domain.WishlistProductRepository

@Service
class WishlistService(
    private val wishlistProductRepository: WishlistProductRepository,
    private val productRepository: ProductRepository,
) {
    fun addWishlist(
        request: AddWishlistRequest,
        currentMember: CurrentMember,
    ): AddWishlistResponse {
        if (productRepository.existsById(request.productId).not()) {
            throw ProductNotFoundException(request.productId)
        }

        val wishlistProduct =
            WishlistProduct(productId = request.productId, memberId = currentMember.id).let {
                wishlistProductRepository.findByIdOrNull(it.id) ?: wishlistProductRepository.save(it)
            }

        return AddWishlistResponse(
            wishlistProduct,
        )
    }

    fun deleteWishlist(
        productId: Long,
        currentMember: CurrentMember,
    ) {
        val id = WishlistProductId(productId = productId, memberId = currentMember.id)
        val wishlistProduct = wishlistProductRepository.findByIdOrNull(id) ?: return

        wishlistProductRepository.delete(wishlistProduct)
    }
}
