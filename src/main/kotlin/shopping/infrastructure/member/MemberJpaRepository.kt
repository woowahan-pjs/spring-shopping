package shopping.infrastructure.member

import org.springframework.data.jpa.repository.JpaRepository
import shopping.domain.member.Member

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean
}
