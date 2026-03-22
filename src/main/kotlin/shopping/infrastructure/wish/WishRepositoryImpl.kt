package shopping.infrastructure.wish

import org.springframework.stereotype.Component
import shopping.domain.wish.Wish
import shopping.domain.wish.WishRepository

@Component
class WishRepositoryImpl(
    private val wishJpaRepository: WishJpaRepository,
) : WishRepository {
    override fun findByMemberId(memberId: Long): List<Wish> = wishJpaRepository.findByMemberId(memberId)

    override fun findByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Wish? = wishJpaRepository.findByMemberIdAndProductId(memberId, productId)

    override fun existsByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Boolean = wishJpaRepository.existsByMemberIdAndProductId(memberId, productId)

    override fun save(wish: Wish): Wish = wishJpaRepository.save(wish)

    override fun deleteById(id: Long) {
        wishJpaRepository.deleteById(id)
    }
}
