package shopping.product.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import shopping.fake.FakeTransactionTemplate
import shopping.global.exception.ApplicationException
import shopping.global.infra.TransactionTemplate
import shopping.product.domain.Product
import shopping.product.fixture.ProductFixture

@DisplayName("ProductCommandService 테스트")
class ProductCommandServiceTest : BehaviorSpec({
    val profanityValidator: ProfanityValidator = mockk()
    val productCommandRepository: ProductCommandRepository = mockk()
    val productQueryRepository: ProductQueryRepository = mockk()
    val transactionTemplate: TransactionTemplate = FakeTransactionTemplate()

    val productCommandService =
        ProductCommandService(profanityValidator, productCommandRepository, productQueryRepository, transactionTemplate)

    Given("상품을 등록할 때") {

        When("상품 이름에 비속어와 특수문자가 없는 경우") {
            val productCreateCommand = ProductFixture.`상품 1`.`상품 생성 COMMAND 생성`()

            every { profanityValidator.containsProfanity(ProductFixture.`상품 1`.productName!!) } returns false
            every { productCommandRepository.save(any()) } returns ProductFixture.`상품 1`.`상품 엔티티 생성`()

            val actual = productCommandService.createProduct(productCreateCommand)

            Then("상품을 정상 저장 후 반환 한다") {
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

        When("상품 이름에 비속어가 포함 된 경우") {
            val productCreateCommand = ProductFixture.`비속어 상품`.`상품 생성 COMMAND 생성`()

            every { profanityValidator.containsProfanity(ProductFixture.`비속어 상품`.productName!!) } returns true

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    productCommandService.createProduct(productCreateCommand)
                }.message shouldBe "상품 이름에 비속어가 포함되어 있습니다"
            }
        }
    }

    Given("상품을 수정 할 때") {

        When("수정 하고자 하는 상품 이름에 비속어와 특수문자가 없는 경우") {
            val productModifyCommand = ProductFixture.`상품 1`.`상품 수정 COMMAND 생성`()
            val product: Product = mockk()

            every { profanityValidator.containsProfanity(ProductFixture.`상품 1`.productName!!) } returns false
            every { productQueryRepository.findByIdAndNotDeleted(any()) } returns product
            justRun { product.modify(any()) }

            productCommandService.modifyProduct(1L, productModifyCommand)

            Then("상품을 수정 한다") {
                verify(exactly = 1) { product.modify(any()) }
            }
        }

        When("수정 하고자 하는 상품 이름에 비속어가 포함 된 경우") {
            val productModifyCommand = ProductFixture.`비속어 상품`.`상품 수정 COMMAND 생성`()

            every { profanityValidator.containsProfanity(ProductFixture.`비속어 상품`.productName!!) } returns true

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    productCommandService.modifyProduct(1L, productModifyCommand)
                }.message shouldBe "상품 이름에 비속어가 포함되어 있습니다"
            }
        }

        When("수정 하고자 하는 상품을 찾을 수 없으면") {
            val productModifyCommand = ProductFixture.`상품 1`.`상품 수정 COMMAND 생성`()

            every { profanityValidator.containsProfanity(ProductFixture.`상품 1`.productName!!) } returns false
            every { productQueryRepository.findByIdAndNotDeleted(any()) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    productCommandService.modifyProduct(1L, productModifyCommand)
                }.message shouldBe "상품을 찾을 수 없습니다."
            }
        }
    }

    Given("상품을 삭제 할 때") {

        When("삭제 하고자 하는 상품이 존재 하면") {
            val product: Product = mockk()

            every { productQueryRepository.findByIdAndNotDeleted(any()) } returns product
            justRun { product.delete() }

            productCommandService.deletedProduct(1L)

            Then("상품을 수정 한다") {
                verify(exactly = 1) { product.delete() }
            }
        }

        When("삭제 하고자 하는 상품을 찾을 수 없으면") {
            every { productQueryRepository.findByIdAndNotDeleted(any()) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    productCommandService.deletedProduct(1L)
                }.message shouldBe "상품을 찾을 수 없습니다."
            }
        }
    }
})
