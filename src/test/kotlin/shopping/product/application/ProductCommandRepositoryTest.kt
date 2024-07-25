package shopping.product.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import shopping.product.fixture.ProductFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("ProductCommandRepository 테스트")
class ProductCommandRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: ProductCommandRepository

    init {
        Given("상품 도메인을 받아") {
            val product = ProductFixture.`상품 1`.`상품 엔티티 생성`()

            When("저장 후") {
                val actual = repository.save(product)

                Then("DB ID 를 변경 후 반환 한다") {
                    actual.id shouldNotBe 0
                }
            }
        }
    }
}
