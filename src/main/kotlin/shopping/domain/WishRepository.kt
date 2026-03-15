package shopping.domain

interface WishRepository {
    fun save(wish: Wish): Wish

    fun findById(id: Long): Wish?

    fun findAllByMemberId(memberId: Long): List<Wish>

    fun deleteById(id: Long)
}
