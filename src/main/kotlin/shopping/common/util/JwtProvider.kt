package shopping.common.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

const val ONE_WEEK_MILLISECOND: Long = 1000 * 60 * 60 * 24 * 7

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    @Value("\${jwt.expiration-millis-second}")
    private val expirationMillisSecond: Long = ONE_WEEK_MILLISECOND,
) {
    private val signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(subject: String): String {
        val now = Date()
        val expireAt = Date(now.time + expirationMillisSecond)

        return Jwts
            .builder()
            .subject(subject)
            .issuedAt(now)
            .expiration(expireAt)
            .signWith(signingKey)
            .compact()
    }
}
