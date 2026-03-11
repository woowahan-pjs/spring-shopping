package shopping.storage.db

import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long> {
    fun findByEmail(email: String): MemberEntity?

    fun existsByEmail(email: String): Boolean
}
