package shopping.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.application.dto.CreateWishCommand
import shopping.application.dto.WishResult
import shopping.domain.ProductRepository
import shopping.domain.Wish
import shopping.domain.WishRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Service
@Transactional(readOnly = true)
class WishService(
    private val wishRepository: WishRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun create(command: CreateWishCommand): Long {
        validateProductExists(command.productId)

        val wish = Wish(memberId = command.memberId, productId = command.productId)
        return wishRepository.save(wish).id
    }

    fun getWishes(memberId: Long): List<WishResult> {
        val wishes = wishRepository.findAllByMemberId(memberId)
        return wishes.map { WishResult.from(it) }
    }

    @Transactional
    fun delete(
        wishId: Long,
        memberId: Long,
    ) {
        val wish = findWishById(wishId)
        wish.validateOwnership(memberId)
        wishRepository.deleteById(wishId)
    }

    private fun validateProductExists(productId: Long) {
        productRepository.findById(productId)
            ?: throw CoreException(ErrorType.PRODUCT_NOT_FOUND)
    }

    private fun findWishById(wishId: Long): Wish = wishRepository.findById(wishId) ?: throw CoreException(ErrorType.WISH_ITEM_NOT_FOUND)
}
