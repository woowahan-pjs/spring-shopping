package shopping.auth.fixture

import shopping.auth.application.command.TokenRefreshCommand
import shopping.auth.domain.AuthenticationCredentials
import shopping.auth.presentation.dto.request.TokenRefreshRequest

enum class TokenFixture(
    val jti: String?,
    val accessToken: String?,
    val refreshToken: String?,
) {
    // 정상 토큰
    `토큰 1`("jti1", "accessToken1", "refreshToken1"),
    `토큰 2`("jti2", "accessToken2", "refreshToken2"),

    // 비정상 토큰
    `REFRESH TOKEN 공백`("jti", "accessToken", ""),
    `REFRESH TOKEN NULL`("jti", "accessToken", null),
    ;

    fun `토큰 엔티티 생성`(): AuthenticationCredentials = AuthenticationCredentials(jti!!, accessToken!!, refreshToken!!)
    fun `토큰 재발급 COMMAND 생성`(): TokenRefreshCommand = TokenRefreshCommand(refreshToken!!)
    fun `토큰 재발급 요청 DTO 생성`(): TokenRefreshRequest = TokenRefreshRequest(refreshToken)
}
