package shopping.auth.presentation.dto.response

import shopping.auth.domain.AuthenticationCredentials

class LoginResponse(authenticationCredentials: AuthenticationCredentials) {
    val accessToken: String = authenticationCredentials.accessToken
    val refreshToken: String = authenticationCredentials.refreshToken
}
