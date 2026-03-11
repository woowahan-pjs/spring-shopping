package shopping.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import shopping.IntegrationTestSupport
import shopping.application.dto.CreateWishCommand
import shopping.storage.db.MemberEntity
import shopping.storage.db.MemberJpaRepository
import shopping.storage.db.ProductEntity
import shopping.storage.db.ProductJpaRepository
import shopping.storage.db.WishJpaRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@DisplayName("위시 리스트 추가, 조회 및 삭제")
class WishServiceTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var wishService: WishService

    @Autowired
    private lateinit var wishJpaRepository: WishJpaRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Nested
    @DisplayName("위시 리스트 추가 시")
    inner class DescribeCreate {
        @Test
        fun `정상적으로 상품을 추가하면 위시 리스트에 저장된다`() {
            // given
            val member = memberJpaRepository.save(MemberEntity("user@test.com", "encoded"))
            val product = productJpaRepository.save(ProductEntity("치킨", 20000, "http://image.png"))
            val command = CreateWishCommand(member.id, product.id)

            // when
            val wishId = wishService.create(command)

            // then
            val saved = wishJpaRepository.findById(wishId).get()
            assertThat(saved.memberId).isEqualTo(member.id)
            assertThat(saved.productId).isEqualTo(product.id)
        }

        @Test
        fun `존재하지 않는 상품을 추가하려 하면 예외가 발생한다`() {
            // given
            val member = memberJpaRepository.save(MemberEntity("user2@test.com", "encoded"))
            val command = CreateWishCommand(member.id, 999L)

            // when & then
            val exception = assertThrows<CoreException> { wishService.create(command) }
            assertThat(exception.errorType).isEqualTo(ErrorType.PRODUCT_NOT_FOUND)
        }
    }

    @Nested
    @DisplayName("위시 리스트 조회 시")
    inner class DescribeGetWishes {
        @Test
        fun `본인의 위시 리스트를 조회하면 추가한 상품 목록이 반환된다`() {
            // given
            val member = memberJpaRepository.save(MemberEntity("user3@test.com", "encoded"))
            val product1 = productJpaRepository.save(ProductEntity("피자", 15000, "http://img1.png"))
            val product2 = productJpaRepository.save(ProductEntity("파스타", 12000, "http://img2.png"))
            wishService.create(CreateWishCommand(member.id, product1.id))
            wishService.create(CreateWishCommand(member.id, product2.id))

            // when
            val wishes = wishService.getWishes(member.id)

            // then
            assertThat(wishes).hasSize(2)
            assertThat(wishes.map { it.productId }).containsExactlyInAnyOrder(product1.id, product2.id)
        }
    }

    @Nested
    @DisplayName("위시 리스트 삭제 시")
    inner class DescribeDelete {
        @Test
        fun `본인 위시 아이템을 삭제하면 해당 항목이 목록에서 제거된다`() {
            // given
            val member = memberJpaRepository.save(MemberEntity("user4@test.com", "encoded"))
            val product = productJpaRepository.save(ProductEntity("버거", 8000, "http://img.png"))
            val wishId = wishService.create(CreateWishCommand(member.id, product.id))

            // when
            wishService.delete(wishId, member.id)

            // then
            assertThat(wishService.getWishes(member.id)).isEmpty()
        }

        @Test
        fun `다른 회원의 위시 아이템을 삭제하려 하면 예외가 발생한다`() {
            // given
            val owner = memberJpaRepository.save(MemberEntity("owner@test.com", "encoded"))
            val other = memberJpaRepository.save(MemberEntity("other@test.com", "encoded"))
            val product = productJpaRepository.save(ProductEntity("샐러드", 7000, "http://img.png"))
            val wishId = wishService.create(CreateWishCommand(owner.id, product.id))

            // when & then
            val exception = assertThrows<CoreException> { wishService.delete(wishId, other.id) }
            assertThat(exception.errorType).isEqualTo(ErrorType.WISH_ACCESS_DENIED)
        }

        @Test
        fun `존재하지 않는 위시 아이템을 삭제하려 하면 예외가 발생한다`() {
            // given
            val member = memberJpaRepository.save(MemberEntity("user5@test.com", "encoded"))

            // when & then
            val exception = assertThrows<CoreException> { wishService.delete(999L, member.id) }
            assertThat(exception.errorType).isEqualTo(ErrorType.WISH_ITEM_NOT_FOUND)
        }
    }
}
