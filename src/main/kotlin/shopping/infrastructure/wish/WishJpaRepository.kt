package shopping.infrastructure.wish

import org.springframework.data.jpa.repository.JpaRepository
import shopping.domain.wish.Wish

interface WishJpaRepository : JpaRepository<Wish, Long> {
    fun findByMemberId(memberId: Long) : List<Wish>
    fun findByMemberIdAndProductId(memberId: Long, productId: Long) : Wish?
    fun existsByMemberIdAndProductId(memberId: Long, productId: Long) : Boolean
}