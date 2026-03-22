package shopping.application.member

import shopping.domain.member.Member
import shopping.domain.member.MemberRepository
import shopping.support.error.CoreException
import org.springframework.stereotype.Service
import shopping.support.error.ErrorType

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun register(email: String, password: String): Member {
        if (memberRepository.existsByEmail(email)) {
            throw CoreException(ErrorType.CONFLICT, "사용자가 이미 존재합니다.")
        }
        return memberRepository.save(
            Member(
                email = email,
                password = password
            )
        )
    }

    fun login(email: String, password: String) : Long {
        val member = memberRepository.findByEmail(email)
        ?: throw CoreException(ErrorType.BAD_REQUEST, "사용자 존재하지 않습니다.")

        if (!member.checkPassword(password)) {
            throw CoreException(ErrorType.BAD_REQUEST, "비밀번호가 틀렸습니다.")
        }
        return member.id;
    }
}