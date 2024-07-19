package shopping.wishlist.application

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import shopping.common.domain.CurrentUser

@Service
class WishlistQueryService(
    @PersistenceContext
    private val entityManager: EntityManager,
) {
    fun findAllByUserId(
        currentUser: CurrentUser,
        pageable: Pageable,
    ): Page<WishlistProductDto> {
        val jpql =
            """
            select new shopping.wishlist.application.WishlistProductDto(
                wp.id.productId,
                p.name,
                p.price,
                p.imageUrl,
                wp.createdAt
            )
            $fromClause
            ${whereClause(currentUser.id)}
            order by wp.createdAt desc
            """.trimIndent()

        val query =
            entityManager
                .createQuery(jpql, WishlistProductDto::class.java)
                .setFirstResult(pageable.offset.toInt())
                .setMaxResults(pageable.pageSize)

        val content = query.resultList
        val totalSize = getTotalCount(currentUser.id)

        return PageImpl(content, pageable, totalSize)
    }

    fun getTotalCount(userId: Long): Long {
        val jpql =
            """
            select count(wp)
            $fromClause
            ${whereClause(userId)}
            """.trimIndent()

        val query = entityManager.createQuery(jpql, Long::class.java)
        return query.singleResult
    }

    private val fromClause =
        """
        from WishlistProduct wp
        join Product p on wp.id.productId = p.id
        """.trimIndent()

    private fun whereClause(userId: Long) =
        """
        where wp.id.userId = $userId
        """.trimIndent()
}
