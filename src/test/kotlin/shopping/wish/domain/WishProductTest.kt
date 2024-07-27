package shopping.wish.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import shopping.product.domain.Product
import shopping.wish.fixture.WishProductFixture

class WishProductTest : BehaviorSpec({
    Given("다른 인스턴스 변수의 위시 리스트와 동등한 객체인지 확인할 때") {
        val product: Product = mockk(relaxed = true)
        val wishList: WishList = mockk(relaxed = true)

        val wishProduct = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`엔티티 생성`(product, wishList)

        When("해당 객체의 상품, 위시 리스트 와 동일하다면") {
            val sameWishProduct = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`엔티티 생성`(product, wishList)

            val actual = wishProduct.isSameWishProduct(sameWishProduct)

            Then("true 를 반환 한다") {
                actual shouldBe true
            }
        }

        When("해당 객체의 상품, 위시 리스트 와 다르다면") {
            val sameWishProduct = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`엔티티 생성`(mockk(relaxed = true), mockk(relaxed = true))

            val actual = wishProduct.isSameWishProduct(sameWishProduct)

            Then("false 를 반환 한다") {
                actual shouldBe false
            }
        }
    }
})
