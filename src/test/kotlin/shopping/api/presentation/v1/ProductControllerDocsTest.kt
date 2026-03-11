package shopping.api.presentation.v1

import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import shopping.api.presentation.v1.dto.CreateProductRequest
import shopping.api.presentation.v1.dto.UpdateProductRequest
import shopping.api.presentation.v1.validator.ProductValidator
import shopping.application.ProductService
import shopping.application.dto.ProductResult
import shopping.test.RestDocsTest

@Tag("restdocs")
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

    @Test
    fun `상품 단건 조회 API가 정상적으로 문서화된다`() {
        val result = ProductResult(1L, "멋진 셔츠", 15000, "http://image.url")
        whenever(productService.getProduct(1L)).thenReturn(result)

        mockController(ProductController(productService, productValidator))
            .pathParam("id", 1L)
            .get("/api/v1/products/{id}")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "product-get",
                    pathParameters(parameterWithName("id").description("상품 ID")),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data.id").description("상품 ID"),
                        fieldWithPath("data.name").description("상품 이름"),
                        fieldWithPath("data.price").description("상품 가격"),
                        fieldWithPath("data.imageUrl").description("상품 이미지 URL"),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `상품 목록 조회 API가 정상적으로 문서화된다`() {
        val results = listOf(ProductResult(1L, "셔츠", 15000, "url1"))
        whenever(productService.getProducts()).thenReturn(results)

        mockController(ProductController(productService, productValidator))
            .get("/api/v1/products")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "product-list",
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data[].id").description("상품 ID"),
                        fieldWithPath("data[].name").description("상품 이름"),
                        fieldWithPath("data[].price").description("상품 가격"),
                        fieldWithPath("data[].imageUrl").description("상품 이미지 URL"),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `상품 수정 API가 정상적으로 문서화된다`() {
        val request = UpdateProductRequest("새 이름", 20000, "http://new.url")
        doNothing().whenever(productValidator).validateUpdate(any())

        mockController(ProductController(productService, productValidator))
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .put("/api/v1/products/{id}")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "product-update",
                    pathParameters(parameterWithName("id").description("상품 ID")),
                    requestFields(
                        fieldWithPath("name").description("수정할 이름"),
                        fieldWithPath("price").description("수정할 가격"),
                        fieldWithPath("imageUrl").description("수정할 이미지 URL"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data").description("데이터 (성공 시 null)").optional(),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `상품 삭제 API가 정상적으로 문서화된다`() {
        mockController(ProductController(productService, productValidator))
            .pathParam("id", 1L)
            .delete("/api/v1/products/{id}")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "product-delete",
                    pathParameters(parameterWithName("id").description("상품 ID")),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data").description("데이터").optional(),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }
}
