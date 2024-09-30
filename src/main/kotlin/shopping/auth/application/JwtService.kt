package shopping.auth.application

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import shopping.auth.domain.AuthenticationCredentials
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode
import shopping.member.domain.Member
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    private val tokenProvider: TokenProvider,
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper,
) {
    fun getUsername(token: String): String = tokenProvider.extractUsername(token).isValidClaimFromToken()

    fun getJti(token: String): String = tokenProvider.extractJti(token).isValidClaimFromToken()

    private fun String?.isValidClaimFromToken(): String {
        if (isNullOrBlank()) {
            throw ApplicationException(ErrorCode.EMPTY_CLAIM)
        }

        return this
    }

    fun generateAuthenticationCredentials(member: Member): AuthenticationCredentials {
        val jti = UUID.randomUUID().toString()

        val accessToken = generateAccessToken(member, jti)
        val refreshToken = generateRefreshToken(member, jti)

        return AuthenticationCredentials(jti, accessToken, refreshToken)
    }

    fun generateAccessToken(
        userDetails: UserDetails,
        jti: String,
    ): String = tokenProvider.buildToken(userDetails, jti, jwtProperties.accessTokenExpiration)

    fun generateRefreshToken(
        userDetails: UserDetails,
        jti: String,
    ): String = tokenProvider.buildToken(userDetails, jti, jwtProperties.refreshTokenExpiration)

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean = (getUsername(token) == userDetails.username) && isTokenActive(token)

    private fun isTokenActive(token: String): Boolean {
        tokenProvider.extractExpiration(token)?.let {
            return it.after(Date())
        }

        return false
    }

    fun checkTokenExpiredByTokenString(token: String): Boolean {
        val parts = token.split(".")

        checkValidTokenParts(parts)

        val payload = String(Base64.getDecoder().decode(parts[1]))

        val expiration = extractExpiration(payload)

        val current = ZonedDateTime.now().toInstant().toEpochMilli() / 1000

        return expiration <= current
    }

    private fun checkValidTokenParts(parts: List<String>) {
        if (parts.size != 3) {
            throw ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN)
        }
    }

    private fun extractExpiration(payload: String): Long {
        objectMapper.readValue(payload, object : TypeReference<MutableMap<String, String>>() {})["exp"]?.let {
            return it.toLong()
        }

        throw ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN)
    }
}
