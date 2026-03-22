package shopping.domain.wish

interface WishRepository {
    fun findByMemberId(memberId: Long): List<Wish>

    fun findByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Wish?

    fun existsByMemberIdAndProductId(
        memberId: Long,
        productId: Long,
    ): Boolean

    fun save(wish: Wish): Wish

    fun deleteById(id: Long)
}
