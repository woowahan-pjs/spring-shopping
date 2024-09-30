package shopping.auth.presentation.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import shopping.auth.application.command.LoginCommand

data class LoginRequest(
    @field:NotBlank(message = "이메일은 필수로 입력하셔야 합니다.")
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    var email: String?,
    @field:NotBlank(message = "비밀번호는 필수로 입력하셔야 합니다.")
    var loginPassword: String?,
) {
    fun toCommand(): LoginCommand = LoginCommand(this.email!!, this.loginPassword!!)
}
