package shopping.member.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import shopping.common.auth.JwtProvider
import shopping.member.domain.EncryptedPassword
import shopping.member.domain.Member
import shopping.member.domain.MemberRepository
import shopping.member.domain.PasswordMismatchException
import shopping.member.domain.UserAlreadyRegisteredException

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun regist(request: MemberRegistRequest): MemberRegistResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw UserAlreadyRegisteredException(request.email)
        }

        Member(
            email = request.email,
            password = EncryptedPassword.from(request.password),
        ).let { memberRepository.save(it) }

        val jwt = jwtProvider.createToken(request.email)

        return MemberRegistResponse(accessToken = jwt)
    }

    fun login(request: MemberLoginRequest): MemberLoginResponse {
        val user =
            memberRepository.findByEmail(request.email)
                ?: throw MemberNotFoundException.fromEmail(request.email)

        if (user.password != EncryptedPassword.from(request.password)) {
            throw PasswordMismatchException()
        }

        val jwt = jwtProvider.createToken(request.email)

        return MemberLoginResponse(accessToken = jwt)
    }
}
