package shopping.api.presentation.v1

import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import shopping.api.presentation.v1.dto.CreateProductRequest
import shopping.api.presentation.v1.validator.ProductValidator
import shopping.application.ProductService
import shopping.application.dto.ProductResult
import shopping.test.RestDocsTest
import kotlin.test.Test

@DisplayName("상품 API 문서화 테스트")
class ProductControllerDocsTest : RestDocsTest() {
    private lateinit var productService: ProductService
    private lateinit var productValidator: ProductValidator

    @BeforeEach
    fun init() {
        productService = mock(ProductService::class.java)
        productValidator = mock(ProductValidator::class.java)
    }

    @Test
    fun `상품 생성 API가 정상적으로 문서화된다`() {
        // given
        val controller = ProductController(productService, productValidator)
        val request = CreateProductRequest("멋진 셔츠", 15000, "http://image.url")
        val result = ProductResult(1L, "멋진 셔츠", 15000, "http://image.url")

        doNothing().`when`(productValidator).validateCreate(request)
        `when`(productService.create(request.toCommand())).thenReturn(1L)
        `when`(productService.getProduct(1L)).thenReturn(result)

        // when & then
        mockController(controller)
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/products")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "product-create",
                    requestFields(
                        fieldWithPath("name").description("등록할 상품 이름 (최대 15자)"),
                        fieldWithPath("price").description("상품 가격"),
                        fieldWithPath("imageUrl").description("상품 이미지 URL"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과 (SUCCESS/ERROR)"),
                        fieldWithPath("data.id").description("생성된 상품 ID"),
                        fieldWithPath("data.name").description("상품 이름"),
                        fieldWithPath("data.price").description("상품 가격"),
                        fieldWithPath("data.imageUrl").description("상품 이미지 URL"),
                        fieldWithPath("error").description("에러 정보 (성공 시 null)").optional(),
                    ),
                ),
            )
    }
}
