package shopping.product.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.springframework.beans.factory.annotation.Autowired
import shopping.product.fixture.ProductFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("ProfanityValidator 테스트")
class ProfanityValidatorTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var validator: ProfanityValidator

    init {
        Given("상품 이름에 비속어가 포함 되어 있는지 확인할 때") {

            When("상품 이름에 비속어가 없다면") {
                val productName = ProductFixture.`상품 1`.productName

                val actual = validator.containsProfanity(productName!!)

                Then("false 를 반환 한다") {
                    actual.shouldBeFalse()
                }
            }

            When("상품 이름에 비속어가 포함되었다면") {
                val productName = ProductFixture.`비속어 상품`.productName

                val actual = validator.containsProfanity(productName!!)

                Then("ture 를 반환 한다") {
                    actual.shouldBeTrue()
                }
            }
        }
    }
}
