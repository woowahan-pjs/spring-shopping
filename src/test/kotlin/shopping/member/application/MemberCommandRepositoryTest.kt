package shopping.member.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.fixture.MemberFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("MemberCommandRepository 테스트")
class MemberCommandRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var memberCommandRepository: MemberCommandRepository

    init {
        Given("새로운 회원 객체를 저장할 때") {
            val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()

            When("저장에 성공하면") {
                memberCommandRepository.save(member)

                Then("정상적인 ID 를 생성한다.") {
                    member.id shouldNotBe 0L
                }
            }
        }
    }
}
