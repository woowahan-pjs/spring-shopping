package shopping.global.exception

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@DisplayName("공통 예외 클래스 테스트")
class ApplicationExceptionTest : BehaviorSpec({
    Given("예외 인스턴스 변수가 생성될 때") {
        When("전달 받은 message 가 있다면") {
            Then("전달 받은 message 를 프로퍼티로 갖는다") {
                val applicationException =
                    ApplicationException(ErrorCode.DUPLICATED_REGISTER_EMAIL, message = "이미 존재 합니다.")

                applicationException.message shouldBe "이미 존재 합니다."
            }
        }

        When("전달 받은 message 가 없다면") {
            Then("ErrorCode 의 message 를 프로퍼티로 갖는다") {
                val applicationException = ApplicationException(ErrorCode.DUPLICATED_REGISTER_EMAIL)

                applicationException.message shouldBe "이미 존재 하는 이메일 입니다."
            }
        }
    }
})
