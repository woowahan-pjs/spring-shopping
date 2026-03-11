package shopping.storage.db

import org.springframework.stereotype.Repository
import shopping.domain.Member
import shopping.domain.MemberRepository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun save(member: Member): Member {
        val entity = MemberEntity.from(member)
        val savedEntity = memberJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByEmail(email: String): Member? {
        val entity =
            memberJpaRepository.findByEmail(email)
                ?: return null
        return entity.toDomain()
    }

    override fun existsByEmail(email: String): Boolean = memberJpaRepository.existsByEmail(email)
}
