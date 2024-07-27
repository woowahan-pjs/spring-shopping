package shopping.auth.application

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal
annotation class LoginMember
