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
import shopping.api.presentation.v1.dto.RegisterMemberRequest
import shopping.api.presentation.v1.validator.MemberValidator
import shopping.application.MemberService
import shopping.application.dto.AuthTokenResult
import shopping.application.dto.LoginMemberCommand
import shopping.test.RestDocsTest
import kotlin.test.Test

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
        val loginCommand = LoginMemberCommand(request.email, request.password)
        `when`(memberService.login(loginCommand)).thenReturn(authResult)

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
                        fieldWithPath("accessToken").description("발급된 인증 토큰"),
                    ),
                ),
            )
    }
}
