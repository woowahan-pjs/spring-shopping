package shopping.application.member

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.instancio.Instancio
import org.instancio.Select.field
import shopping.domain.member.Member
import shopping.support.config.jpa.BaseEntity
import shopping.interfaces.api.member.MemberV1Dto

class MemberFacadeTest : FunSpec({

    val memberService = mockk<MemberService>()
    val tokenService = mockk<TokenService>()
    val memberFacade = MemberFacade(memberService, tokenService)

    test("회원가입 요청을 MemberService에 위임한다") {
        val request = MemberV1Dto.RegisterRequest(
            email = "test@test.com",
            password = "password123"
        )
        val member = Instancio.of(Member::class.java)
            .set(field("email"), request.email)
            .set(field("password"), request.password)
            .create()

        every { memberService.register(request.email, request.password) } returns member

        val result = memberFacade.register(request)

        result.email shouldBe request.email
    }

    test("로그인 성공 시 토큰을 반환한다") {
        val request = MemberV1Dto.LoginRequest(
            email = "test@test.com",
            password = "password123"
        )
        val member = Instancio.create(Member::class.java)
        val token = "jwt-token"

        every { memberService.login(request.email, request.password) } returns member.id
        every { tokenService.generateToken(member.id, request.email) } returns token

        val result = memberFacade.login(request)

        result shouldBe token
    }
})
