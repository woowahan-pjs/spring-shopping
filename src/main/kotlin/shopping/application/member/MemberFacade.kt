package shopping.application.member

import org.springframework.stereotype.Service
import shopping.domain.member.Member
import shopping.interfaces.api.member.MemberV1Dto

@Service
class MemberFacade(
    private val memberService: MemberService,
    private val tokenService: TokenService,
) {
    fun register(request: MemberV1Dto.RegisterRequest): Member = memberService.register(request.email, request.password)

    fun login(request: MemberV1Dto.LoginRequest): String {
        val memberId = memberService.login(request.email, request.password)
        return tokenService.generateToken(memberId, request.email)
    }
}
