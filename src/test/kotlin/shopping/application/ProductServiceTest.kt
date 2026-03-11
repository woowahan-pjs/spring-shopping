package shopping.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.bean.override.mockito.MockitoBean
import shopping.IntegrationTestSupport
import shopping.application.dto.CreateProductCommand
import shopping.application.dto.UpdateProductCommand
import shopping.domain.ProfanityFilter
import shopping.storage.db.ProductJpaRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType
import kotlin.test.Test

@DisplayName("상품 서비스 테스트")
class ProductServiceTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @MockitoBean
    private lateinit var profanityFilter: ProfanityFilter

    @AfterEach
    fun tearDown() {
        productJpaRepository.deleteAllInBatch()
    }

    @Nested
    @DisplayName("상품 생성 시")
    inner class DescribeCreate {
        @Test
        fun `비속어가 포함되지 않은 정상적인 상품은 저장된다`() {
            // given
            `when`(profanityFilter.containsProfanity(any())).thenReturn(false)
            val command = CreateProductCommand("치킨", 20000, "http://image.png")

            // when
            val productId = productService.create(command)

            // then
            val savedProduct = productJpaRepository.findById(productId).get()
            assertThat(savedProduct.name).isEqualTo("치킨")
        }

        @Test
        fun `이름에 비속어가 포함되면 예외가 발생한다`() {
            // given
            `when`(profanityFilter.containsProfanity(any())).thenReturn(true)
            val command = CreateProductCommand("나쁜말", 10000, "http://image.png")

            // when & then
            val exception = assertThrows<CoreException> { productService.create(command) }
            assertThat(exception.errorType).isEqualTo(ErrorType.PRODUCT_PROFANITY_DETECTED)
        }
    }

    @Nested
    @DisplayName("상품 수정 시")
    inner class DescribeUpdate {
        @Test
        fun `존재하는 상품의 정보를 수정할 수 있다`() {
            // given
            whenever(profanityFilter.containsProfanity(any())).thenReturn(false)
            val productId = productService.create(CreateProductCommand("콜라", 1500, "url"))
            val updateCommand = UpdateProductCommand("제로콜라", 2000, "new-url")

            // when
            productService.update(productId, updateCommand)

            // then
            val updated = productService.getProduct(productId)
            assertThat(updated.name).isEqualTo("제로콜라")
            assertThat(updated.price).isEqualTo(2000)
        }
    }

    @Nested
    @DisplayName("상품 삭제 시")
    inner class DescribeDelete {
        @Test
        fun `상품을 삭제하면 더 이상 조회되지 않는다`() {
            // given
            `when`(profanityFilter.containsProfanity(any())).thenReturn(false)
            val productId = productService.create(CreateProductCommand("삭제할템", 100, "url"))

            // when
            productService.delete(productId)

            // then
            assertThrows<CoreException> { productService.getProduct(productId) }
        }
    }
}
