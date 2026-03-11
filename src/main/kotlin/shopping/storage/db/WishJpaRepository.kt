package shopping.storage.db

import org.springframework.data.jpa.repository.JpaRepository

interface WishJpaRepository : JpaRepository<WishEntity, Long> {
    fun findAllByMemberId(memberId: Long): List<WishEntity>
}
