package shopping.wish.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import shopping.global.exception.ApplicationException
import shopping.wish.fixture.WishProductsFixture

class WishProductsTest : BehaviorSpec({
    Given("위시 리스트에 저장할 상품을 받아") {
        val wishProducts = WishProducts()

        When("상품을 등록 한다") {
            shouldNotThrowAny {
                wishProducts.add(mockk())
            }
        }
    }

    Given("위시 리스트에 저장 된 상품을 삭제할 때") {
        val wishProduct: WishProduct = mockk()
        val wishProducts = WishProductsFixture.`위시 리스트 상품 1개 목록`.`객체 생성`(wishProduct)

        When("제거할 상품이 위시 리스트에 존재 한다면") {
            every { wishProduct.isSameWishProduct(any()) } returns true
            wishProducts.delete(wishProduct)

            Then("위시 리스트 상품을 제거한다") {
                wishProducts shouldHaveSize 0
            }
        }

        When("제거할 상품이 위시 리스트에 없다면") {
            every { wishProduct.isSameWishProduct(any()) } returns false

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    wishProducts.delete(wishProduct)
                }.message shouldBe  "찜 상품을 찾을 수 없습니다."
            }
        }
    }
})
