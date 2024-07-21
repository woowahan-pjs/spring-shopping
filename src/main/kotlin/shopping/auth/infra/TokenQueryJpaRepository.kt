package shopping.auth.infra

import org.springframework.stereotype.Repository
import shopping.auth.application.TokenQueryRepository
import shopping.auth.domain.Token

@Repository
class TokenQueryJpaRepository(private val tokenJpaRepository: TokenJpaRepository) : TokenQueryRepository {
    override fun findByJti(jti: String): Token? =
        tokenJpaRepository.findAll {
            val tokenEntity = entity(Token::class)

            select(
                tokenEntity,
            ).from(
                tokenEntity,
            ).where(
                path(Token::jti).equal(jti),
            )
        }.firstOrNull()
}
