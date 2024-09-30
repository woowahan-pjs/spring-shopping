package shopping.auth.application

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.auth.application.command.LoginCommand
import shopping.auth.application.command.TokenRefreshCommand
import shopping.auth.domain.AuthenticationCredentials
import shopping.global.exception.ApplicationException
import shopping.global.exception.ErrorCode
import shopping.member.application.MemberQueryRepository
import shopping.member.domain.Member

@Service
@Transactional
class AuthenticationCommandService(
    private val tokenCommandRepository: TokenCommandRepository,
    private val tokenQueryRepository: TokenQueryRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {
    fun logIn(loginCommand: LoginCommand): AuthenticationCredentials {
        val member: Member =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginCommand.email, loginCommand.loginPassword),
            ).principal as Member

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenCommandRepository.save(authenticationCredentials)

        return authenticationCredentials
    }

    fun refreshToken(tokenRefreshCommand: TokenRefreshCommand): AuthenticationCredentials {
        val presentedRefreshToken = tokenRefreshCommand.refreshToken

        val member = memberQueryRepository.findByEmailAndNotDeleted(jwtService.getUsername(presentedRefreshToken))

        val foundToken = tokenQueryRepository.findByJti(jwtService.getJti(presentedRefreshToken))
        validateToken(foundToken, presentedRefreshToken, member)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member!!)

        tokenCommandRepository.save(authenticationCredentials)
        tokenCommandRepository.deleteByJti(foundToken!!.jti)

        return authenticationCredentials
    }

    private fun validateToken(
        authenticationCredentials: AuthenticationCredentials?,
        refreshToken: String,
        member: Member?,
    ) {
        if (authenticationCredentials == null) {
            throw ApplicationException(ErrorCode.TOKEN_NOT_FOUND)
        }

        validateMember(member)

        validateRefreshToken(authenticationCredentials, refreshToken, member!!)

        validateActiveAccessToken(authenticationCredentials.accessToken, authenticationCredentials.jti)
    }

    private fun validateMember(member: Member?) {
        if (member == null) {
            throw ApplicationException(ErrorCode.MEMBER_NOT_FOUND)
        }
    }

    private fun validateRefreshToken(
        authenticationCredentials: AuthenticationCredentials,
        refreshToken: String,
        user: UserDetails,
    ) {
        authenticationCredentials.validateRefreshToken(refreshToken)

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN)
        }
    }

    private fun validateActiveAccessToken(
        accessToken: String,
        jti: String,
    ) {
        if (!jwtService.checkTokenExpiredByTokenString(accessToken)) {
            tokenCommandRepository.deleteByJti(jti)
            throw ApplicationException(ErrorCode.INVALID_TOKEN_REISSUE_REQUEST)
        }
    }
}
