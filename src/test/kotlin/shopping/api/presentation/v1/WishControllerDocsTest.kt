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
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import shopping.api.presentation.v1.dto.CreateWishRequest
import shopping.api.presentation.v1.validator.WishValidator
import shopping.application.WishService
import shopping.application.dto.WishResult
import shopping.domain.TokenProvider
import shopping.test.RestDocsTest

@Tag("restdocs")
@DisplayName("위시 리스트 API 문서화 테스트")
class WishControllerDocsTest : RestDocsTest() {
    private lateinit var wishService: WishService
    private lateinit var wishValidator: WishValidator
    private lateinit var tokenProvider: TokenProvider

    @BeforeEach
    fun init() {
        wishService = mock(WishService::class.java)
        wishValidator = mock(WishValidator::class.java)
        tokenProvider = mock(TokenProvider::class.java)
    }

    @Test
    fun `위시 리스트 추가 API가 정상적으로 문서화된다`() {
        // given
        val controller = WishController(wishService, wishValidator, tokenProvider)
        val request = CreateWishRequest(1L)
        val result = WishResult(1L, 1L, 1L)

        whenever(tokenProvider.extractMemberId(any())).thenReturn(1L)
        doNothing().`when`(wishValidator).validateCreate(request)
        `when`(wishService.create(request.toCommand(1L))).thenReturn(1L)
        `when`(wishService.getWishes(1L)).thenReturn(listOf(result))

        // when & then
        mockController(controller)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer mock-token")
            .body(request)
            .post("/api/v1/wishes")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "wish-create",
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer 인증 토큰"),
                    ),
                    requestFields(
                        fieldWithPath("productId").description("추가할 상품 ID"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과 (SUCCESS/ERROR)"),
                        fieldWithPath("data.id").description("생성된 위시 ID"),
                        fieldWithPath("data.memberId").description("회원 ID"),
                        fieldWithPath("data.productId").description("상품 ID"),
                        fieldWithPath("error").description("에러 정보 (성공 시 null)").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `위시 리스트 조회 API가 정상적으로 문서화된다`() {
        // given
        val results = listOf(WishResult(1L, 1L, 2L), WishResult(2L, 1L, 3L))

        whenever(tokenProvider.extractMemberId(any())).thenReturn(1L)
        whenever(wishService.getWishes(1L)).thenReturn(results)

        // when & then
        mockController(WishController(wishService, wishValidator, tokenProvider))
            .header("Authorization", "Bearer mock-token")
            .get("/api/v1/wishes")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "wish-list",
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer 인증 토큰"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data[].id").description("위시 ID"),
                        fieldWithPath("data[].memberId").description("회원 ID"),
                        fieldWithPath("data[].productId").description("상품 ID"),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `위시 리스트 삭제 API가 정상적으로 문서화된다`() {
        // given
        whenever(tokenProvider.extractMemberId(any())).thenReturn(1L)
        doNothing().whenever(wishService).delete(1L, 1L)

        // when & then
        mockController(WishController(wishService, wishValidator, tokenProvider))
            .header("Authorization", "Bearer mock-token")
            .pathParam("id", 1L)
            .delete("/api/v1/wishes/{id}")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "wish-delete",
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer 인증 토큰"),
                    ),
                    pathParameters(
                        parameterWithName("id").description("삭제할 위시 ID"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("data").description("데이터 (성공 시 null)").optional(),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }
}
