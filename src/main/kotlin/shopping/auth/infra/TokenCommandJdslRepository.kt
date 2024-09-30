package shopping.auth.infra

import org.springframework.stereotype.Repository
import shopping.auth.application.TokenCommandRepository
import shopping.auth.domain.Token

@Repository
class TokenCommandJdslRepository(private val tokenJpaRepository: TokenJpaRepository) : TokenCommandRepository {
    override fun save(authenticationCredentials: Token) = tokenJpaRepository.save(authenticationCredentials)

    override fun deleteByJti(jti: String) {
        tokenJpaRepository.delete {
            deleteFrom(
                entity(Token::class),
            ).where(
                path(Token::jti).equal(jti),
            )
        }
    }
}
