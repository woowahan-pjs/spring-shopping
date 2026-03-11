package shopping.infrastructure.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import shopping.domain.TokenProvider
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.token.secret-key}") secretKey: String,
    @Value("\${security.jwt.token.access.expire-length}") private val validityInMilliseconds: Long,
) : TokenProvider {
    // NOTE: HS256 알고리즘을 안전하게 사용하려면 secretKey가 최소 32글자 이상이어야 합니다.
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    override fun generate(memberId: Long): String {
        val now = Instant.now()
        val validity = now.plusMillis(validityInMilliseconds)

        return Jwts
            .builder()
            .subject(memberId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(validity))
            .signWith(key)
            .compact()
    }
}
