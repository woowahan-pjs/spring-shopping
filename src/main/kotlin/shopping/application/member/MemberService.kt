package shopping.application.member

import shopping.domain.member.Member
import shopping.domain.member.MemberRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

class MemberService(
    private val memberRepository: MemberRepository
) {
    fun register(email: String, password: String): Member {
        if (memberRepository.existsByEmail(email)) {
            throw CoreException(ErrorType.INTERNAL_ERROR, "사용자가 이미 존재합니다.")
        }
        return memberRepository.save(
            Member(
                email = email,
                password = password
            )
        )
    }
}