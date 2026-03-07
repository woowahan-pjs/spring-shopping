package shopping.infrastructure.member

import org.springframework.stereotype.Component
import shopping.domain.member.Member
import shopping.domain.member.MemberRepository

@Component
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)
    }

    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email);
    }

    override fun save(member: Member): Member {
        return memberJpaRepository.save(member)
    }
}