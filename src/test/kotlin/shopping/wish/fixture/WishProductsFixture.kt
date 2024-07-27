package shopping.wish.fixture

import shopping.wish.domain.WishProduct
import shopping.wish.domain.WishProducts

enum class WishProductsFixture(
    val wishProducts: Set<WishProductFixture>
) {
    `위시 리스트 상품 1개 목록`(setOf(WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`))
    ;

    fun `객체 생성`(): WishProducts = WishProducts(wishProducts.map { it.`엔티티 생성`() }.toMutableSet())
    fun `객체 생성`(vararg wishProduct: WishProduct): WishProducts = WishProducts(wishProduct.toMutableSet())
}
