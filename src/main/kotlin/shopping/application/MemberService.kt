package shopping.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shopping.application.dto.AuthTokenResult
import shopping.application.dto.LoginMemberCommand
import shopping.application.dto.RegisterMemberCommand
import shopping.domain.Member
import shopping.domain.MemberRepository
import shopping.domain.PasswordEncoder
import shopping.domain.TokenProvider
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun register(command: RegisterMemberCommand): Long {
        checkDuplicateEmail(command.email)
        val member = Member.create(command.email, command.rawPassword, passwordEncoder)
        val savedMember = memberRepository.save(member)

        return savedMember.id
    }

    private fun checkDuplicateEmail(email: String) {
        if (memberRepository.existsByEmail(email)) {
            throw CoreException(ErrorType.MEMBER_DUPLICATE_EMAIL)
        }
    }

    @Transactional(readOnly = true)
    fun login(command: LoginMemberCommand): AuthTokenResult {
        val member = getMemberByEmail(command.email)
        member.authenticate(command.rawPassword, passwordEncoder)
        val token = tokenProvider.generate(member.id)

        return AuthTokenResult(token)
    }

    private fun getMemberByEmail(email: String): Member =
        memberRepository.findByEmail(email)
            ?: throw CoreException(ErrorType.MEMBER_NOT_FOUND_OR_INVALID_PASSWORD)
}
