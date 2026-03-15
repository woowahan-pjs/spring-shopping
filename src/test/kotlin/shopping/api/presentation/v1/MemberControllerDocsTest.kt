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
import shopping.api.presentation.v1.dto.LoginMemberRequest
import shopping.api.presentation.v1.dto.RegisterMemberRequest
import shopping.api.presentation.v1.validator.MemberValidator
import shopping.application.MemberService
import shopping.application.dto.AuthTokenResult
import shopping.test.RestDocsTest

@Tag("restdocs")
@DisplayName("회원 API 문서화 테스트")
class MemberControllerDocsTest : RestDocsTest() {
    private lateinit var memberService: MemberService
    private lateinit var memberValidator: MemberValidator

    @BeforeEach
    fun init() {
        memberService = mock(MemberService::class.java)
        memberValidator = mock(MemberValidator::class.java)
    }

    @Test
    fun `회원가입 API가 정상적으로 문서화된다`() {
        // given
        val controller = MemberController(memberService, memberValidator)
        val request = RegisterMemberRequest("test@test.com", "password")
        val authResult = AuthTokenResult("mock-access-token")

        doNothing().`when`(memberValidator).validateRegister(request)
        `when`(memberService.register(request.toCommand())).thenReturn(1L)
        `when`(memberService.login(any())).thenReturn(authResult)

        // when & then
        mockController(controller)
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/members/register")
            .then()
            .status(HttpStatus.CREATED)
            .apply(
                document(
                    "member-register",
                    requestFields(
                        fieldWithPath("email").description("가입할 회원의 이메일"),
                        fieldWithPath("password").description("가입할 회원의 비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과 (SUCCESS/ERROR)"),
                        fieldWithPath("data.accessToken").description("발급된 인증 토큰"),
                        fieldWithPath("error").description("에러 정보 (성공 시 null)").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `로그인 API가 정상적으로 문서화된다`() {
        // given
        val controller = MemberController(memberService, memberValidator)
        val request = LoginMemberRequest("test@test.com", "password")
        val authResult = AuthTokenResult("mock-access-token")

        doNothing().whenever(memberValidator).validateLogin(request)
        whenever(memberService.login(any())).thenReturn(authResult)

        // when & then
        mockController(controller)
            .contentType(ContentType.JSON)
            .body(request)
            .post("/api/v1/members/login")
            .then()
            .status(HttpStatus.OK)
            .apply(
                document(
                    "member-login",
                    requestFields(
                        fieldWithPath("email").description("회원 이메일"),
                        fieldWithPath("password").description("회원 비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과 (SUCCESS/ERROR)"),
                        fieldWithPath("data.accessToken").description("발급된 인증 토큰"),
                        fieldWithPath("error").description("에러 정보").optional(),
                    ),
                ),
            )
    }
}
