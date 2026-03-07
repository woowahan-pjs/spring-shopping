package shopping.application.member

import shopping.domain.member.Member
import shopping.interfaces.api.member.MemberV1Dto

class MemberFacade(
    private val memberService: MemberService
) {
    fun register(request: MemberV1Dto.RegisterRequest): Member {
        return memberService.register(request.email, request.password);
    }
}