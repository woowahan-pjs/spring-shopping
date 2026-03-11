package shopping.storage.db

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import shopping.domain.Wish
import shopping.domain.WishRepository

@Repository
class WishRepositoryImpl(
    private val wishJpaRepository: WishJpaRepository,
) : WishRepository {
    override fun save(wish: Wish): Wish {
        val entity = WishEntity.from(wish)
        return wishJpaRepository.save(entity).toDomain()
    }

    override fun findById(id: Long): Wish? {
        val entity = wishJpaRepository.findByIdOrNull(id) ?: return null
        return entity.toDomain()
    }

    override fun findAllByMemberId(memberId: Long): List<Wish> = wishJpaRepository.findAllByMemberId(memberId).map { it.toDomain() }

    override fun deleteById(id: Long) {
        wishJpaRepository.deleteById(id)
    }
}
