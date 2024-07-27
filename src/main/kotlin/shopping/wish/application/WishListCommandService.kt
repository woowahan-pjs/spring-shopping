package shopping.wish.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode
import shopping.product.application.ProductQueryService
import shopping.product.domain.Product
import shopping.wish.application.command.WishProductAddCommand
import shopping.wish.application.command.WishProductDeleteCommand
import shopping.wish.domain.WishList
import shopping.wish.domain.WishProduct

@Service
@Transactional
class WishListCommandService(
    private val wishListQueryRepository: WishListQueryRepository,
    private val wishListCommandRepository: WishListCommandRepository,
    private val productQueryService: ProductQueryService,
) {
    fun addWishProduct(memberId: Long, wishProductAddCommand: WishProductAddCommand) {
        val wishList = wishListQueryRepository.findByMemberIdAndNotDeleted(memberId).orCreate(memberId)
        val wishProduct = productQueryService.findProductNotDeleted(wishProductAddCommand.productId).toWishProduct(wishList)

        wishList.addWishProduct(wishProduct)
    }

    private fun WishList?.orCreate(memberId: Long): WishList {
        if (this == null) {
            return wishListCommandRepository.save(WishList(memberId))
        }

        return this
    }

    fun deleteWishProduct(memberId: Long, wishProductDeleteCommand: WishProductDeleteCommand) {
        val wishList = wishListQueryRepository.findByMemberIdAndNotDeleted(memberId)
            ?: throw ApplicationException(ErrorCode.WISH_LIST_NOT_FOUND)

        val wishProduct = productQueryService.findProductNotDeleted(wishProductDeleteCommand.productId).toWishProduct(wishList)

        wishList.deleteWishProduct(wishProduct)
    }

    private fun Product.toWishProduct(wishList: WishList): WishProduct = WishProduct(this, wishList)
}
