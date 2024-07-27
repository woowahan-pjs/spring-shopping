package shopping.member.application

import shopping.member.domain.Member

interface MemberQueryRepository {
    fun existsByEmailAndNotDeleted(email: String?): Boolean

    fun findByIdAndNotDeleted(id: Long): Member?

    fun findByEmailAndNotDeleted(email: String?): Member?
}
