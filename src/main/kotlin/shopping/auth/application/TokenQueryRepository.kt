package shopping.auth.application

import shopping.auth.domain.AuthenticationCredentials

interface TokenQueryRepository {
    fun findByJti(jti: String): AuthenticationCredentials?
}
