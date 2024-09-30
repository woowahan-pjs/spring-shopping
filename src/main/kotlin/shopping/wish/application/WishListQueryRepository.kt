package shopping.wish.application

import shopping.wish.domain.WishList

interface WishListQueryRepository {
    fun findByMemberIdAndNotDeleted(memberId: Long): WishList?
}
