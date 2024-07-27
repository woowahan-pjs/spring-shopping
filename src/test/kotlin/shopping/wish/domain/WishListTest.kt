package shopping.wish.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import shopping.wish.fixture.WishListFixture

class WishListTest : BehaviorSpec({
    Given("위시 리스트의 상품 목록들을") {
        val wishList = WishListFixture.`고객 1의 위시 리스트`.`엔티티 생성`()

        When("적절한 컬렉션으로 변환하여") {
            shouldNotThrowAny {
                wishList.getWishProducts()
            }
        }
    }
})
