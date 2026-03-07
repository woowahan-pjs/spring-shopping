package shopping.infrastructure.wish

import org.springframework.stereotype.Component
import shopping.domain.wish.Wish
import shopping.domain.wish.WishRepository

@Component
class WishRepositoryImpl(
    private val wishJpaRepository: WishJpaRepository
) : WishRepository {
    override fun findByMemberId(memberId: Long): List<Wish> {
        return wishJpaRepository.findByMemberId(memberId)
    }

    override fun findByMemberIdAndProductId(
        memberId: Long,
        productId: Long
    ): Wish? {
        return wishJpaRepository.findByMemberIdAndProductId(memberId, productId)
    }

    override fun existsByMemberIdAndProductId(memberId: Long, productId: Long): Boolean {
        return wishJpaRepository.existsByMemberIdAndProductId(memberId, productId)
    }

}