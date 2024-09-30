package shopping.auth.application

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JjwtTokenProvider(
    jwtProperties: JwtProperties,
) : TokenProvider {
    override val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    override fun extractUsername(token: String): String? = extractAllClaims(token)?.subject

    override fun extractJti(token: String): String? = extractAllClaims(token)?.id

    override fun extractExpiration(token: String): Date? = extractAllClaims(token)?.expiration

    private fun extractAllClaims(token: String): Claims? =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    override fun buildToken(
        userDetails: UserDetails,
        jti: String,
        expiration: Long,
        additionalClaims: Map<String, Any>,
    ): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .add(additionalClaims)
            .id(jti)
            .and()
            .signWith(secretKey)
            .compact()
}
