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

    // NOTE: Wish가 Product 도메인의 Repository를 직접 참조합니다.
    //       멀티 모듈 분리 시 도메인 이벤트 또는 ACL 패턴으로 의존 방향을 역전시켜야 합니다.
    private fun validateProductExists(productId: Long) {
        productRepository.findById(productId)
            ?: throw CoreException(ErrorType.PRODUCT_NOT_FOUND)
    }

    private fun findWishById(wishId: Long): Wish = wishRepository.findById(wishId) ?: throw CoreException(ErrorType.WISH_ITEM_NOT_FOUND)
}
