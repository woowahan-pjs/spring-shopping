package shopping.infrastructure.member

import org.springframework.stereotype.Component
import shopping.domain.member.Member
import shopping.domain.member.MemberRepository

@Component
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun findByEmail(email: String): Member? = memberJpaRepository.findByEmail(email)

    override fun existsByEmail(email: String): Boolean = memberJpaRepository.existsByEmail(email)

    override fun save(member: Member): Member = memberJpaRepository.save(member)
}
