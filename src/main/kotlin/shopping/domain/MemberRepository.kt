package shopping.domain

interface MemberRepository {
    fun save(member: Member): Member

    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean
}
