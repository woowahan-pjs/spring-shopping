package shopping.wish.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import shopping.global.exception.ApplicationException
import shopping.product.application.ProductQueryService
import shopping.wish.domain.WishList
import shopping.wish.fixture.WishProductFixture

class WishListCommandServiceTest : BehaviorSpec({
    val wishListQueryRepository: WishListQueryRepository = mockk()
    val wishListCommandRepository: WishListCommandRepository = mockk()
    val productQueryService: ProductQueryService = mockk()

    val service = WishListCommandService(wishListQueryRepository, wishListCommandRepository, productQueryService)

    Given("회원 ID 와 위시 리스트에 추가할 상품 정보를 받아") {
        val memberId = 1L
        val command = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 등록 COMMAND 생성`()

        When("위시 리스트에 상품을 추가 할때 기존에 위시 리스트가 없는 경우") {
            val wishList: WishList = mockk()

            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns null
            every { wishListCommandRepository.save(any()) } returns wishList
            every { productQueryService.findProductNotDeleted(any()) } returns mockk()
            justRun { wishList.addWishProduct(any()) }

            service.addWishProduct(memberId, command)

            Then("위시 리스트를 새로 생성 후 상품을 추가 한다") {
                verify(exactly = 1) { wishListCommandRepository.save(any()) }
                verify(exactly = 1) { wishList.addWishProduct(any()) }
            }
        }

        When("위시 리스트에 상품을 추가 할때 기존에 위시 리스트가 있는 경우") {
            val wishList: WishList = mockk()

            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns wishList
            every { productQueryService.findProductNotDeleted(any()) } returns mockk()
            justRun { wishList.addWishProduct(any()) }

            service.addWishProduct(memberId, command)

            Then("기존 위시 리스트에 상품을 추가 한다") {
                verify(exactly = 1) { wishList.addWishProduct(any()) }
            }
        }
    }
    
    Given("회원 ID 와 위시 리스트에서 제거할 상품 정보를 받아") {
        val memberId = 1L
        val command = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 제거 COMMAND 생성`()

        When("회원 ID 로 위시 리스트를 찾은 경우") {
            val wishList: WishList = mockk()

            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns wishList
            every { productQueryService.findProductNotDeleted(command.productId) } returns mockk()
            justRun { wishList.deleteWishProduct(any()) }

            service.deleteWishProduct(memberId, command)

            Then("위시 리스트에서 상품을 제거 한다") {
                verify(exactly = 1) { wishList.deleteWishProduct(any()) }
            }
        }

        When("회원 ID 로 위시 리스트를 찾을 수 없는 경우") {
            every { wishListQueryRepository.findByMemberIdAndNotDeleted(memberId) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    service.deleteWishProduct(memberId, command)
                }.message shouldBe "위시 리스트를 찾을 수 없습니다."
            }
        }

    }
})
