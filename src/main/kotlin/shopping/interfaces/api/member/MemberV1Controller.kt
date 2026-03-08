package shopping.interfaces.api.member

import com.failsafe.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.application.member.MemberFacade

@RestController
@RequestMapping("/api/members")
class MemberV1Controller(
    private val memberFacade: MemberFacade,
)  {
    @PostMapping("/register")
    fun register(
        @RequestBody request: MemberV1Dto.RegisterRequest,
    ): ApiResponse<MemberV1Dto.RegisterResponse> {
        return memberFacade.register(request)
            .let { MemberV1Dto.RegisterResponse.from(it) }
            .let { ApiResponse.success(it) }
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: MemberV1Dto.LoginRequest,
    ): ApiResponse<MemberV1Dto.LoginResponse> {
        return memberFacade.login(request)
            .let { MemberV1Dto.LoginResponse.from(it) }
            .let { ApiResponse.success(it) }
    }
}
