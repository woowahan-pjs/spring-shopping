package shopping.wishlist.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import shopping.common.domain.CurrentUser
import shopping.product.application.ProductNotFoundException
import shopping.product.domain.ProductRepository
import shopping.wishlist.domain.WishlistProduct
import shopping.wishlist.domain.WishlistProductRepository

@Service
class WishlistService(
    private val wishlistProductRepository: WishlistProductRepository,
    private val productRepository: ProductRepository,
) {
    fun addWishlist(
        request: AddWishlistRequest,
        currentUser: CurrentUser,
    ): AddWishlistResponse {
        if (productRepository.existsById(request.productId).not()) {
            throw ProductNotFoundException(request.productId)
        }

        val wishlistProduct =
            WishlistProduct(productId = request.productId, userId = currentUser.id).let {
                wishlistProductRepository.findByIdOrNull(it.id)
                    ?: wishlistProductRepository.save(it)
            }

        return AddWishlistResponse(
            wishlistProduct,
        )
    }
}
