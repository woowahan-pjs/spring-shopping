package shopping.application.member

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.instancio.Instancio
import org.instancio.Select.field
import shopping.domain.member.Member
import shopping.domain.member.MemberRepository
import shopping.support.error.CoreException

class MemberServiceTest : BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val memberService = MemberService(memberRepository)

    given("회원가입 시") {

        `when`("이메일이 중복되지 않으면") {
            val member = Instancio.of(Member::class.java)
                .set(field("email"), "test@test.com")
                .set(field("password"), "password123")
                .create()

            every { memberRepository.existsByEmail(member.email) } returns false
            every { memberRepository.save(any()) } returns member

            then("회원가입에 성공한다") {
                val result = memberService.register(member.email, member.password)
                result.email shouldBe member.email
            }
        }

        `when`("이메일이 이미 존재하면") {
            val member = Instancio.create(Member::class.java)

            every { memberRepository.existsByEmail(member.email) } returns true

            then("예외가 발생한다") {
                val exception = shouldThrow<CoreException> {
                    memberService.register(member.email, member.password)
                }
                exception.message shouldBe "사용자가 이미 존재합니다."
            }
        }
    }

    given("로그인 시") {

        `when`("이메일과 비밀번호가 일치하면") {
            val member = Instancio.of(Member::class.java)
                .set(field("email"), "test@test.com")
                .set(field("password"), "password123")
                .create()

            every { memberRepository.findByEmail(member.email) } returns member

            then("멤버 ID를 반환한다") {
                val result = memberService.login(member.email, member.password)
                result shouldBe member.id
            }
        }

        `when`("이메일이 존재하지 않으면") {
            val member = Instancio.create(Member::class.java)

            every { memberRepository.findByEmail(member.email) } returns null

            then("예외가 발생한다") {
                val exception = shouldThrow<CoreException> {
                    memberService.login(member.email, member.password)
                }
                exception.message shouldBe "사용자 존재하지 않습니다."
            }
        }

        `when`("비밀번호가 틀리면") {
            val member = Instancio.of(Member::class.java)
                .set(field("email"), "test@test.com")
                .set(field("password"), "correctPassword")
                .create()

            every { memberRepository.findByEmail(member.email) } returns member

            then("예외가 발생한다") {
                val exception = shouldThrow<CoreException> {
                    memberService.login(member.email, "wrongPassword")
                }
                exception.message shouldBe "비밀번호가 틀렸습니다."
            }
        }
    }
})
