package shopping.wish.fixture

import shopping.product.domain.Product
import shopping.product.fixture.ProductFixture
import shopping.wish.application.command.WishProductAddCommand
import shopping.wish.application.command.WishProductDeleteCommand
import shopping.wish.domain.WishList
import shopping.wish.domain.WishProduct
import shopping.wish.presentation.dto.request.WishProductAddRequest
import shopping.wish.presentation.dto.request.WishProductDeleteRequest

enum class WishProductFixture(
    val product: ProductFixture?,
    val wishList: WishListFixture
) {
    // 정상 상품
    `고객 1의 위시 리스트에 저장 된 상품 1`(ProductFixture.`상품 1`, WishListFixture.`고객 1의 위시 리스트`),

    // 비정상 상품
    `상품 정보 null`(null, WishListFixture.`고객 1의 위시 리스트`),
    ;

    fun `엔티티 생성`(): WishProduct = WishProduct(product!!.`상품 엔티티 생성`(), wishList.`엔티티 생성`())
    fun `엔티티 생성`(product: Product, wishList: WishList): WishProduct = WishProduct(product, wishList)
    fun `위시 리스트 상품 등록 COMMAND 생성`(): WishProductAddCommand = WishProductAddCommand(product!!.`상품 엔티티 생성`().id)
    fun `위시 리스트 상품 등록 COMMAND 생성`(product: Product): WishProductAddCommand = WishProductAddCommand(product.id)
    fun `위시 리스트 상품 등록 REQUEST 생성`(): WishProductAddRequest = WishProductAddRequest(product?.`상품 엔티티 생성`()?.id)
    fun `위시 리스트 상품 제거 COMMAND 생성`(): WishProductDeleteCommand = WishProductDeleteCommand(product!!.`상품 엔티티 생성`().id)
    fun `위시 리스트 상품 제거 COMMAND 생성`(product: Product): WishProductDeleteCommand = WishProductDeleteCommand(product.id)
    fun `위시 리스트 상품 제거 REQUEST 생성`(): WishProductDeleteRequest = WishProductDeleteRequest(product?.`상품 엔티티 생성`()?.id)
}
