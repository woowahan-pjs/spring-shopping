package shopping.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode

typealias Token = AuthenticationCredentials

@Entity
@Table(
    name = "authentication_credentials",
    uniqueConstraints = [UniqueConstraint(name = "uk_authentication_credentials_jti", columnNames = ["jti"])],
)
class AuthenticationCredentials(
    jti: String,
    accessToken: String,
    refreshToken: String,
) {
    @field:Column(name = "jti", nullable = false)
    var jti: String = jti
        protected set

    @field:Column(name = "access_token", nullable = false)
    var accessToken: String = accessToken
        protected set

    @field:Column(name = "refresh_token", nullable = false)
    var refreshToken: String = refreshToken
        protected set

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    fun validateRefreshToken(refreshToken: String) {
        if (this.refreshToken != refreshToken) {
            throw ApplicationException(ErrorCode.MISS_MATCH_TOKEN)
        }
    }
}
