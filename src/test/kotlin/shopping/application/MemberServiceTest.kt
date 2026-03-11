package shopping.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import shopping.IntegrationTestSupport
import shopping.application.dto.LoginMemberCommand
import shopping.application.dto.RegisterMemberCommand
import shopping.storage.db.MemberJpaRepository
import shopping.support.error.CoreException
import shopping.support.error.ErrorType
import kotlin.test.Test

@DisplayName("회원 가입 및 로그인")
class MemberServiceTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    private val email = "test@test.com"
    private val rawPassword = "password"

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAllInBatch()
    }

    @Nested
    @DisplayName("회원 가입 시")
    inner class DescribeRegister {
        @Test
        fun `정상적으로 회원가입을 완료하면 회원 정보가 저장된다`() {
            // given
            val command = RegisterMemberCommand(email, rawPassword)

            // when
            val resultId = memberService.register(command)

            // then
            val users = memberJpaRepository.findAll()

            assertAll(
                { assertThat(resultId).isGreaterThan(0L) },
                { assertThat(users).hasSize(1) },
                { assertThat(users[0].email).isEqualTo(email) },
                { assertThat(users[0].password).isNotEqualTo(rawPassword) },
            )
        }

        @Test
        fun `이미 존재하는 이메일로 가입을 시도하면 예외가 발생한다`() {
            // given
            val command = RegisterMemberCommand(email, rawPassword)
            memberService.register(command)

            // when & then
            val exception =
                assertThrows<CoreException> {
                    memberService.register(command)
                }
            assertThat(exception.errorType).isEqualTo(ErrorType.MEMBER_DUPLICATE_EMAIL)
        }
    }

    @Nested
    @DisplayName("로그인 시")
    inner class DescribeLogin {
        @Test
        fun `이메일과 비밀번호가 일치하면 토큰을 발급한다`() {
            // given: 정상 가입된 유저
            val registerCommand = RegisterMemberCommand(email, rawPassword)
            memberService.register(registerCommand)
            val loginCommand = LoginMemberCommand(email, rawPassword)

            // when
            val result = memberService.login(loginCommand)

            // then
            assertThat(result.accessToken).isNotEmpty()
        }

        @Test
        fun `가입되지 않은 이메일이거나 비밀번호가 틀리면 예외가 발생한다`() {
            // given
            val registerCommand = RegisterMemberCommand(email, rawPassword)
            memberService.register(registerCommand)

            val loginCommand = LoginMemberCommand(email, "wrong-password")

            // when & then
            val exception =
                assertThrows<CoreException> {
                    memberService.login(loginCommand)
                }
            assertThat(exception.errorType).isEqualTo(ErrorType.MEMBER_NOT_FOUND_OR_INVALID_PASSWORD)
        }
    }
}
