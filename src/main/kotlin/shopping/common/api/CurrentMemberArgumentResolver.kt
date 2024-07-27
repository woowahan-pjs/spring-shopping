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
import shopping.common.domain.CurrentMember
import shopping.member.application.MemberNotFoundException
import shopping.member.domain.MemberRepository

@Component
class CurrentMemberArgumentResolver(
    private val jwtProvider: JwtProvider,
    private val memberRepository: MemberRepository,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == CurrentMember::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val token = getToken(webRequest)
        val email = getEmail(token)

        memberRepository.findByEmail(email)?.let {
            return CurrentMember(it.id, it.email)
        } ?: throw MemberNotFoundException.fromEmail(email)
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
