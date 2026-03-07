package shopping.domain.member

import java.util.*

interface MemberRepository {
    fun findByEmail(email: String): Member?
    fun existsByEmail(email: String): Boolean
}