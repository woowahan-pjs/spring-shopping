package shopping.wish.application

import io.kotest.assertions.throwables.shouldNotThrowAny
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.fixture.MemberFixture
import shopping.member.infra.MemberJpaRepository
import shopping.product.fixture.ProductFixture
import shopping.product.infra.ProductJpaRepository
import shopping.support.KotestIntegrationTestSupport
import shopping.wish.fixture.WishListFixture
import shopping.wish.fixture.WishProductFixture
import shopping.wish.infra.WishListJpaRepository
import shopping.wish.infra.WishProductJpaRepository

class WishListCommandServiceIntegrationTest : KotestIntegrationTestSupport() {

    @Autowired
    private lateinit var service: WishListCommandService

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var wishListJpaRepository: WishListJpaRepository

    @Autowired
    private lateinit var wishProductJpaRepository: WishProductJpaRepository

    init {
        Given("회원 ID 와 위시 리스트에 추가할 상품 정보를 받아") {
            val member = memberJpaRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val product = productJpaRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())
            val command = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 등록 COMMAND 생성`(product)

            Then("위시 리스트에 상품을 추가 한다") {
                shouldNotThrowAny {
                    service.addWishProduct(member.id, command)
                }
            }
        }

        Given("회원 ID 와 위시 리스트에서 제거할 상품 정보를 받아") {
            val member = memberJpaRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val wishList = wishListJpaRepository.save(WishListFixture.`고객 1의 위시 리스트`.`엔티티 생성`(member))
            val product = productJpaRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())
            wishProductJpaRepository.save(WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`엔티티 생성`(product, wishList))
            val command = WishProductFixture.`고객 1의 위시 리스트에 저장 된 상품 1`.`위시 리스트 상품 제거 COMMAND 생성`(product)

            Then("위시 리스트에서 상품을 제거 한다") {
                shouldNotThrowAny {
                    service.deleteWishProduct(member.id, command)
                }
            }
        }
    }
}
