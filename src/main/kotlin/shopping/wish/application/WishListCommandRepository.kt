package shopping.wish.application

import shopping.wish.domain.WishList

interface WishListCommandRepository {
    fun save(wishList: WishList): WishList
}
