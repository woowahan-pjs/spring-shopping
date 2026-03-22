package shopping.interfaces.api.member

import com.failsafe.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.application.member.MemberFacade

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberFacade: MemberFacade,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody request: MemberDto.RegisterRequest,
    ): ApiResponse<MemberDto.RegisterResponse> =
        memberFacade
            .register(request)
            .let { MemberDto.RegisterResponse.from(it) }
            .let { ApiResponse.success(it) }

    @PostMapping("/login")
    fun login(
        @RequestBody request: MemberDto.LoginRequest,
    ): ApiResponse<MemberDto.LoginResponse> =
        memberFacade
            .login(request)
            .let { MemberDto.LoginResponse.from(it) }
            .let { ApiResponse.success(it) }
}
