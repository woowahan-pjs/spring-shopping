package shopping.wish.infra

import org.springframework.stereotype.Repository
import shopping.wish.application.WishListQueryRepository
import shopping.wish.domain.WishList

@Repository
class WishListQueryJdslRepository(
    private val wishListJpaRepository: WishListJpaRepository,
) : WishListQueryRepository {
    override fun findByMemberIdAndNotDeleted(memberId: Long): WishList? = wishListJpaRepository.findAll {
        val entity = entity(WishList::class)

        select(
            entity
        ).from(
            entity
        ).where(
            path(WishList::deletedAt).isNull()
        )
    }.firstOrNull()
}
