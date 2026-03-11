package shopping.api.presentation.v1

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import shopping.api.presentation.v1.dto.AuthTokenResponse
import shopping.api.presentation.v1.dto.LoginMemberRequest
import shopping.api.presentation.v1.dto.RegisterMemberRequest
import shopping.api.presentation.v1.validator.MemberValidator
import shopping.application.MemberService
import shopping.application.dto.LoginMemberCommand

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
    private val memberValidator: MemberValidator,
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @RequestBody request: RegisterMemberRequest,
    ): AuthTokenResponse {
        memberValidator.validateRegister(request)

        memberService.register(request.toCommand())

        val loginCommand = LoginMemberCommand(request.email, request.password)
        val authResult = memberService.login(loginCommand)

        return AuthTokenResponse(authResult.accessToken)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginMemberRequest,
    ): AuthTokenResponse {
        memberValidator.validateLogin(request)

        val authResult = memberService.login(request.toCommand())
        return AuthTokenResponse(authResult.accessToken)
    }
}
