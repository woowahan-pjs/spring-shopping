package shopping.member.application

import shopping.member.domain.Member

interface MemberCommandRepository {
    fun save(member: Member): Member
}
