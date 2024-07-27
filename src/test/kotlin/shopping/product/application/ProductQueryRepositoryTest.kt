package shopping.product.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import shopping.product.fixture.ProductFixture
import shopping.product.infra.ProductJpaRepository
import shopping.support.KotestIntegrationTestSupport

@DisplayName("ProductQueryRepository 테스트")
class ProductQueryRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: ProductQueryRepository
    @Autowired
    private lateinit var jpaRepository: ProductJpaRepository

    init {
        Given("상품 ID 로 저장 된 상품을 찾을 때") {

            When("저장 된 상품 중 삭제가 되지 않으면서 ID 가 일치하는 상품을 찾으면") {
                val product = jpaRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())

                val actual = repository.findByIdAndNotDeleted(product.id)

                Then("반환 한다") {
                    actual.shouldNotBeNull()
                }
            }

            When("저장 된 상품 중 삭제가 되지 않으면서 ID 가 일치하는 상품이 없으면") {
                val productId = 1L

                val actual = repository.findByIdAndNotDeleted(productId)

                Then("null 을 반환 한다") {
                    actual.shouldBeNull()
                }
            }
        }

        Given("전체 상품 조회를 할 때") {

            When("삭제 되지 않은 저장 된 상품이 있다면") {
                jpaRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())

                val actual = repository.findAllAndNotDeleted()

                Then("1개 이상 반환 한다") {
                    actual.shouldNotBeEmpty()
                }
            }

            When("삭제 되지 않은 저장 된 상품이 없다면") {
                val product = jpaRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())
                product.delete()
                jpaRepository.save(product)

                val actual = repository.findAllAndNotDeleted()

                Then("빈 컬렉션을 반환 한다") {
                    actual.shouldBeEmpty()
                }
            }
        }
    }
}
