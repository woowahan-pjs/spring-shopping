package shopping.member.presentation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.global.common.SuccessResponse
import shopping.member.application.MemberCommandService
import shopping.member.application.MemberQueryService
import shopping.member.presentation.dto.request.MemberRegisterRequest
import shopping.member.presentation.dto.response.MemberRegisterResponse

@RestController
class MemberApi(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService
) {
    @PostMapping("/api/members")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid request: MemberRegisterRequest): SuccessResponse<MemberRegisterResponse> {
        val id = memberCommandService.createMember(request.toCommand())
        return SuccessResponse(MemberRegisterResponse(memberQueryService.findById(id)), HttpStatus.CREATED)
    }
}
