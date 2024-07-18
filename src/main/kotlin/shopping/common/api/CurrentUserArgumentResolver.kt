package shopping.common.api

import io.jsonwebtoken.JwtException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import shopping.common.domain.CurrentUser
import shopping.common.error.LoginFailedException
import shopping.common.util.JwtProvider
import shopping.user.application.UserNotFoundException
import shopping.user.domain.UserRepository

@Component
class CurrentUserArgumentResolver(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == CurrentUser::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val token = getToken(webRequest)
        val email = getEmail(token)

        userRepository.findByEmail(email)?.let {
            return CurrentUser(it.id, it.email)
        } ?: throw UserNotFoundException.fromEmail(email)
    }

    private fun getEmail(token: String) =
        try {
            jwtProvider.getSubject(token)
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw LoginFailedException("인증정보가 유효하지 않습니다.", e)
                else -> throw e
            }
        }

    private fun getToken(webRequest: NativeWebRequest): String {
        val token = webRequest.getHeader(AUTHORIZATION) ?: throw LoginFailedException("인증정보가 없습니다.")
        return token.split(" ").getOrNull(1) ?: throw LoginFailedException("인증정보가 유효하지 않습니다.")
    }
}
