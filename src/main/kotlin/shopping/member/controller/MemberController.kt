package shopping.member.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.member.application.MemberLoginRequest
import shopping.member.application.MemberLoginResponse
import shopping.member.application.MemberRegistRequest
import shopping.member.application.MemberRegistResponse
import shopping.member.application.MemberService

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/regist")
    fun regist(
        @Valid @RequestBody request: MemberRegistRequest,
    ): ApiResponse<MemberRegistResponse> {
        val response = memberService.regist(request)

        return ApiResponse.success(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: MemberLoginRequest,
    ): ApiResponse<MemberLoginResponse> {
        val response = memberService.login(request)

        return ApiResponse.success(response)
    }
}
