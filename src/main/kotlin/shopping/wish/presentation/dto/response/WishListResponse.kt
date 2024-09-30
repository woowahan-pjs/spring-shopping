package shopping.wish.presentation.dto.response

import shopping.product.presentation.dto.response.ProductResponse
import shopping.wish.domain.WishProduct

class WishListResponse(
    wishList: Set<WishProduct>
) {
    val wishList = wishList.map { wishProduct -> ProductResponse(wishProduct.product) }
}
