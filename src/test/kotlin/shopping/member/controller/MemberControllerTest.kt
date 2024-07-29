package shopping.member.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import shopping.E2ETest
import shopping.common.error.ErrorCode
import shopping.member.application.MemberLoginRequest
import shopping.member.application.MemberRegistRequest

class MemberControllerTest : E2ETest() {
    @Test
    fun `회원가입 성공`() {
        val response = `회원가입 요청`(memberRegistRequest()).extract().jsonPath()

        assertThat(response.getString("data.accessToken")).isNotBlank()
    }

    @ValueSource(strings = ["", "short", "123456789012345678901"])
    @ParameterizedTest
    fun `비밀번호 형식에 맞지않으면 가입 불가능`(password: String) {
        `회원가입 요청`(memberRegistRequest(password = password))
            .statusCode(400)
    }

    @Test
    fun `잘못된 이메일 형식 가입 불가능`() {
        `회원가입 요청`(memberRegistRequest(email = "invalidemail.com"))
            .statusCode(400)
    }

    @Test
    fun `이름 미기입시 가입 불가능`() {
        `회원가입 요청`(memberRegistRequest(name = ""))
            .statusCode(400)
    }

    @Test
    fun `로그인 성공`() {
        val registRequest = memberRegistRequest()
        `회원가입 요청`(registRequest)
        val loginRequest =
            memberLoginRequest(
                email = registRequest.email,
                password = registRequest.password,
            )

        val response = `로그인 요청`(loginRequest)

        assertThat(response.getString("data.accessToken")).isNotBlank()
    }

    @Test
    fun `비밀번호 불일치시 로그인 실패`() {
        val registRequest = memberRegistRequest()
        `회원가입 요청`(registRequest)
        val loginRequest =
            memberLoginRequest(
                email = registRequest.email,
                password = registRequest.password + "pad",
            )

        val response = `로그인 요청`(loginRequest)

        assertThat(response.getString("error.code")).isEqualTo(ErrorCode.PASSWORD_MISMATCH.code)
    }

    private fun `회원가입 요청`(request: MemberRegistRequest) =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/members/regist")
            .then()

    private fun `로그인 요청`(request: MemberLoginRequest) =
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/members/login")
            .then()
            .extract()
            .jsonPath()

    private fun memberRegistRequest(
        email: String = "test@email.com",
        password: String = "password!!@#",
        name: String = "name",
    ) = MemberRegistRequest(
        name = name,
        email = email,
        password = password,
    )

    private fun memberLoginRequest(
        email: String = "",
        password: String = "",
    ) = MemberLoginRequest(
        email = email,
        password = password,
    )
}
