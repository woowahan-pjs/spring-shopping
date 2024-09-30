package shopping.auth.presentation.dto.response

import shopping.auth.domain.AuthenticationCredentials

class TokenRefreshResponse(authenticationCredentials: AuthenticationCredentials) {
    val refreshToken: String = authenticationCredentials.refreshToken
}
