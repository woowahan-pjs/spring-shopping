package shopping.domain.member

interface MemberRepository {
    fun findByEmail(email: String): Member?
    fun existsByEmail(email: String): Boolean
    fun save(member: Member) : Member
}