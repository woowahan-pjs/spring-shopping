package shopping.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import shopping.global.exception.ApplicationException
import shopping.member.fixture.MemberFixture

@DisplayName("MemberQueryService 테스트")
class MemberQueryServiceTest : BehaviorSpec({

    val memberQueryRepository: MemberQueryRepository = mockk()
    val memberQueryService = MemberQueryService(memberQueryRepository)

    Given("회원 ID 를 이용해 탈퇴 하지 않은 회원을 조회할 때") {
        val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()
        val memberId = member.id

        When("ID 가 일치 하면서 탈퇴 하지 않은 회원이 존재 한다면") {
            every { memberQueryRepository.findByIdAndNotDeleted(memberId) } returns member

            val actual = memberQueryService.findById(memberId)

            Then("회원 엔티티를 반환한다") {
                actual shouldBe member
            }
        }

        When("ID 가 일치 하면서 탈퇴 하지 않은 회원이 없다면") {
            every { memberQueryRepository.findByIdAndNotDeleted(memberId) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    memberQueryService.findById(memberId)
                } shouldHaveMessage "일치하는 회원 정보를 찾을 수 없습니다."
            }
        }
    }
})
