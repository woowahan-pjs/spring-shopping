package shopping.member.infra

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import shopping.member.application.MemberQueryRepository
import shopping.member.domain.Member

@Repository
class MemberQueryJdslRepository(
    private val memberJpaRepository: MemberJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: RenderContext,
): MemberQueryRepository {
    override fun existsByEmailAndNotDeleted(email: String?): Boolean {
        val query = jpql {
            val memberEntity = entity(Member::class)

            select(
                memberEntity
            ).from(
                memberEntity
            ).where(
                isSameEmailAndNotDeleted(email)
            )
        }

        return entityManager
            .createQuery(query, jpqlRenderContext)
            .apply { setMaxResults(1) }
            .resultList.isNotEmpty()
    }

    override fun findByIdAndNotDeleted(id: Long): Member? = memberJpaRepository.findAll {
        val memberEntity = entity(Member::class)

        select(
            memberEntity
        ).from(
            memberEntity
        ).where(
            path(Member::deletedAt).isNull()
                .and(
                    path(Member::id).equal(id)
                )
        )
    }.firstOrNull()

    override fun findByEmailAndNotDeleted(email: String?): Member? = memberJpaRepository.findAll {
        val memberEntity = entity(Member::class)

        select(
            memberEntity
        ).from(
            memberEntity
        ).where(
            isSameEmailAndNotDeleted(email)
        )
    }.firstOrNull()

    private fun Jpql.isSameEmailAndNotDeleted(email: String?) =
        path(Member::deletedAt).isNull()
            .and(
                path(Member::email).eq(email)
            )
}
