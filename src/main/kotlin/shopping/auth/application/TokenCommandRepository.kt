package shopping.auth.application

import shopping.auth.domain.AuthenticationCredentials

interface TokenCommandRepository {
    fun save(authenticationCredentials: AuthenticationCredentials): AuthenticationCredentials

    fun deleteByJti(jti: String)
}
