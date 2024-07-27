package shopping.common.api

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import shopping.common.auth.InvalidTokenException
import shopping.common.auth.JwtProvider
import shopping.common.auth.TokenExpiredException
import shopping.common.auth.TokenMissingException
import shopping.common.domain.CurrentUser
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
                is ExpiredJwtException -> throw TokenExpiredException()
                is JwtException, is IllegalArgumentException -> throw InvalidTokenException(e)
                else -> throw e
            }
        }

    private fun getToken(webRequest: NativeWebRequest): String {
        val token = webRequest.getHeader(AUTHORIZATION) ?: throw TokenMissingException()
        return token.split(" ").getOrNull(1) ?: throw InvalidTokenException()
    }
}
