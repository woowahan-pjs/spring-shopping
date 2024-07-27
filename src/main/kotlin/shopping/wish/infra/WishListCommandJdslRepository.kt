package shopping.wish.infra

import org.springframework.stereotype.Repository
import shopping.wish.application.WishListCommandRepository
import shopping.wish.domain.WishList

@Repository
class WishListCommandJdslRepository(
    private val wishListJpaRepository: WishListJpaRepository
) : WishListCommandRepository {
    override fun save(wishList: WishList): WishList = wishListJpaRepository.save(wishList)
}
