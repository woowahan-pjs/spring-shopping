package shopping.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder
import shopping.global.exception.ApplicationException
import shopping.member.domain.Member
import shopping.member.fixture.MemberFixture

@DisplayName("MemberCommandService 테스트")
class MemberCommandServiceTest : BehaviorSpec({

    val memberCommandRepository: MemberCommandRepository = mockk()
    val memberQueryRepository: MemberQueryRepository = mockk()
    val passwordEncoder: PasswordEncoder = mockk()

    val memberCommandService = MemberCommandService(memberCommandRepository, memberQueryRepository, passwordEncoder)

    Given("새로운 회원을 생성할 때") {
        val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()
        val memberCommand = MemberFixture.`고객 1`.`회원 생성 COMMAND 생성`()

        When("기존 회원 중 중복된 email 이 없는 경우") {
            every { passwordEncoder.encode(member.password) } returns "encodedPassword"
            every { memberQueryRepository.existsByEmailAndNotDeleted(memberCommand.email) } returns false
            every { memberCommandRepository.save(any<Member>()) } returns member

            val actual = memberCommandService.createMember(memberCommand)

            Then("회원을 생성 후 key 값을 반환 한다") {
                actual.shouldNotBeNull()
            }
        }

        When("기존 회원 중 중복된 email 이 존재 하는 경우") {
            every { memberQueryRepository.existsByEmailAndNotDeleted(memberCommand.email) } returns true

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    memberCommandService.createMember(memberCommand)
                } shouldHaveMessage "이미 존재 하는 이메일 입니다."
            }
        }
    }
})
