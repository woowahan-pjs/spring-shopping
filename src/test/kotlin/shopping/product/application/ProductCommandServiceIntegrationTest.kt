package shopping.product.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import shopping.product.domain.Product
import shopping.product.fixture.ProductFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("ProductCommandService 통합 테스트")
class ProductCommandServiceIntegrationTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var productCommandService: ProductCommandService
    @Autowired
    private lateinit var productCommandRepository: ProductCommandRepository
    @Autowired
    private lateinit var productQueryRepository: ProductQueryRepository

    init {
        Given("상품 정보를 받아") {
            val productCreateCommand = ProductFixture.`상품 1`.`상품 생성 COMMAND 생성`()

            When("상품을 등록 후") {
                val actual = productCommandService.createProduct(productCreateCommand)

                Then("반환 한다") {
                    actual.shouldBeEqualToUsingFields(
                        ProductFixture.`상품 1`.`상품 엔티티 생성`(),
                        Product::sellingPrice,
                        Product::fixedPrice,
                        Product::productName,
                        Product::productAmount,
                        Product::productImage,
                        Product::productDescription,
                    )
                }
            }
        }

        Given("상품 ID 와 수정 정보를 받아") {
            val product = productCommandRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())
            val productUpdateCommand = ProductFixture.`상품 2`.`상품 수정 COMMAND 생성`()

            When("동일한 ID의 상품을 수정 한다") {
                productCommandService.modifyProduct(product.id, productUpdateCommand)

                Then("상품을 수정 후 다시 조회 하면 변경 된 정보가 조회 된다") {
                    val actual = productQueryRepository.findByIdAndNotDeleted(product.id)
                    println("actual?.sellingPrice = ${actual?.sellingPrice}")
                    println("ProductFixture.`상품 2`.`상품 엔티티 생성`().sellingPrice = ${ProductFixture.`상품 2`.`상품 엔티티 생성`().sellingPrice}")

                    actual.shouldNotBeNull().shouldBeEqualToUsingFields(
                        ProductFixture.`상품 2`.`상품 엔티티 생성`(),
                        Product::sellingPrice,
                        Product::fixedPrice,
                        Product::productName,
                        Product::productAmount,
                        Product::productImage,
                        Product::productDescription,
                    )
                }

            }
        }

        Given("상품 ID 를 받아") {
            val product = productCommandRepository.save(ProductFixture.`상품 1`.`상품 엔티티 생성`())

            When("동일한 ID 의 상품을 삭제 한다") {
                productCommandService.deletedProduct(product.id)

                Then("상품을 삭제 하면 해당 상품은 조회되지 않는다") {
                    val actual = productQueryRepository.findByIdAndNotDeleted(product.id)
                    actual.shouldBeNull()
                }
            }
        }

    }
}
