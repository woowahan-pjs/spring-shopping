package shopping.wishlist.application

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import shopping.common.domain.CurrentMember

@Service
class WishlistQueryService(
    @PersistenceContext
    private val entityManager: EntityManager,
) {
    fun findAllByUserId(
        currentMember: CurrentMember,
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
            ${whereClause(currentMember.id)}
            order by wp.createdAt desc
            """.trimIndent()

        val query =
            entityManager
                .createQuery(jpql, WishlistProductDto::class.java)
                .setFirstResult(pageable.offset.toInt())
                .setMaxResults(pageable.pageSize)

        val content = query.resultList
        val totalSize = getTotalCount(currentMember.id)

        return PageImpl(content, pageable, totalSize)
    }

    fun getTotalCount(memberId: Long): Long {
        val jpql =
            """
            select count(wp)
            $fromClause
            ${whereClause(memberId)}
            """.trimIndent()

        val query = entityManager.createQuery(jpql, Long::class.java)
        return query.singleResult
    }

    private val fromClause =
        """
        from WishlistProduct wp
        join Product p on wp.id.productId = p.id
        """.trimIndent()

    private fun whereClause(memberId: Long) =
        """
        where wp.id.memberId = $memberId
        """.trimIndent()
}
