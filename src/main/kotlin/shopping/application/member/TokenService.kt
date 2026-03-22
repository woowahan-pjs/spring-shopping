package shopping.application.member

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date

@Service
class TokenService(
    @Value("\${jwt.secret-key}") secretKeyString: String,
) {
    private val secretKey: Key = Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    private val expirationMillis = 1000 * 60 * 60 // 1시간

    fun generateToken(
        memberId: Long,
        email: String,
    ): String {
        val now = Date()
        val expiry = Date(now.time + expirationMillis)

        return Jwts
            .builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

            true
        } catch (e: Exception) {
            false
        }

    fun getMemberId(token: String): Long {
        val claims = getClaims(token)

        return claims.subject.toLong()
    }

    fun getEmail(token: String): String {
        val claims = getClaims(token)

        return claims["email"] as String
    }

    private fun getClaims(token: String): Claims =
        Jwts
            .parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
}
