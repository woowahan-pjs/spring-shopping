package shopping.application.wish

import shopping.domain.product.ProductRepository
import shopping.domain.wish.Wish
import shopping.domain.wish.WishRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

class WishService(
    private val wishRepository: WishRepository,
    private val productRepository: ProductRepository
) {
    fun addWish(memberId: Long, productId: Long): Wish {
        if (!productRepository.existsById(productId)) {
            throw CoreException(ErrorType.NOT_FOUND, "상품이 존재하지 않습니다.")
        }
        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw CoreException(ErrorType.CONFLICT, "이미 위시리스트에 추가된 상품입니다.")
        }
        return wishRepository.save(Wish(memberId = memberId, productId = productId))
    }

    fun removeWish(memberId: Long, wishId: Long) {
        val wish = wishRepository.findByMemberId(memberId)
            .find { it.id == wishId }
            ?: throw CoreException(ErrorType.NOT_FOUND, "위시리스트에 존재하지 않는 항목입니다.")
        wishRepository.deleteById(wish.id)
    }

    fun getWishes(memberId: Long): List<Wish> {
        return wishRepository.findByMemberId(memberId)
    }
}
