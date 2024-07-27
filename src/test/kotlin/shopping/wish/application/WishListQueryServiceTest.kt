package shopping.wish.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import shopping.wish.domain.WishList
import shopping.wish.fixture.WishProductsFixture

class WishListQueryServiceTest : BehaviorSpec({
    val wishListQueryRepository: WishListQueryRepository = mockk()

    val service = WishListQueryService(wishListQueryRepository)

    Given("회원 ID 를 받아 위시 리스트를 조회할 때") {
        val memberId = 1L

        When("해당 회원의 위시 리스트가 존재 한다면") {
            val wishList: WishList = mockk()
            val wishProducts = WishProductsFixture.`위시 리스트 상품 1개 목록`.`객체 생성`()

            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns wishList
            every { wishList.getWishProducts() } returns wishProducts.toSet()

            val actual = service.findWishList(memberId)

            Then("위시 리스트 상품 목록을 반환 한다") {
                actual.shouldHaveSize(1)
            }
        }

        When("해당 회원의 위시 리스트가 없다면") {
            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns null

            val actual = service.findWishList(memberId)

            Then("빈 위시 리스트를 반환 한다") {
                actual.shouldBeEmpty()
            }
        }
    }
})
