package shopping.user.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shopping.common.api.ApiResponse
import shopping.user.application.UserRegistRequest
import shopping.user.application.UserRegistResponse
import shopping.user.application.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/regist")
    fun regist(
        @Valid @RequestBody request: UserRegistRequest,
    ): ApiResponse<UserRegistResponse> {
        val response = userService.regist(request)

        return ApiResponse.success(response)
    }
}
