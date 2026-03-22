package shopping.application.wish

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.instancio.Instancio
import shopping.domain.product.ProductRepository
import shopping.domain.wish.Wish
import shopping.domain.wish.WishRepository
import shopping.support.error.CoreException

class WishServiceTest :
    BehaviorSpec({

        val wishRepository = mockk<WishRepository>()
        val productRepository = mockk<ProductRepository>()
        val wishService = WishService(wishRepository, productRepository)

        given("위시리스트에 상품을 추가할 때") {

            val memberId = 1L
            val productId = 100L

            `when`("정상적인 요청이면") {
                val wish = Wish(memberId, productId)

                every { productRepository.existsById(productId) } returns true
                every { wishRepository.existsByMemberIdAndProductId(memberId, productId) } returns false
                every { wishRepository.save(any()) } returns wish

                then("위시리스트에 저장된다") {
                    val result = wishService.addWish(memberId, productId)
                    result.memberId shouldBe memberId
                    result.productId shouldBe productId
                    verify { wishRepository.save(any()) }
                }
            }

            `when`("존재하지 않는 상품이면") {
                every { productRepository.existsById(productId) } returns false

                then("예외가 발생한다") {
                    val exception =
                        shouldThrow<CoreException> {
                            wishService.addWish(memberId, productId)
                        }
                    exception.message shouldBe "상품이 존재하지 않습니다."
                }
            }

            `when`("이미 위시리스트에 있는 상품이면") {
                every { productRepository.existsById(productId) } returns true
                every { wishRepository.existsByMemberIdAndProductId(memberId, productId) } returns true

                then("예외가 발생한다") {
                    val exception =
                        shouldThrow<CoreException> {
                            wishService.addWish(memberId, productId)
                        }
                    exception.message shouldBe "이미 위시리스트에 추가된 상품입니다."
                }
            }
        }

        given("위시리스트에서 상품을 제거할 때") {

            val memberId = 1L
            val wishId = 50L

            `when`("정상적으로 제거 요청하면") {
                // ID를 직접 설정하는 대신 Instancio가 생성한 객체를 사용
                val wish = Instancio.create(Wish::class.java)

                every { wishRepository.findByMemberId(memberId) } returns listOf(wish)
                every { wishRepository.deleteById(any()) } returns Unit

                then("상품이 위시리스트에서 제거된다") {
                    wishService.removeWish(memberId, wish.id)
                    verify { wishRepository.deleteById(wish.id) }
                }
            }

            `when`("본인의 위시리스트 항목이 아니면") {
                every { wishRepository.findByMemberId(memberId) } returns emptyList()

                then("예외가 발생한다") {
                    val exception =
                        shouldThrow<CoreException> {
                            wishService.removeWish(memberId, wishId)
                        }
                    exception.message shouldBe "위시리스트에 존재하지 않는 항목입니다."
                }
            }
        }

        given("위시리스트 목록을 조회할 때") {

            val memberId = 1L

            `when`("조회 요청하면") {
                // memberId를 생성자로 직접 주입하여 정확성 보장
                val wishes = List(3) { Wish(memberId, it.toLong()) }

                every { wishRepository.findByMemberId(memberId) } returns wishes

                then("사용자의 위시리스트 목록을 반환한다") {
                    val result = wishService.getWishes(memberId)
                    result.size shouldBe 3
                    result.all { it.memberId == memberId } shouldBe true
                }
            }
        }
    })
