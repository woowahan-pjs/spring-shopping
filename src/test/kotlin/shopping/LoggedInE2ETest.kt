package shopping

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.application.MemberRegistRequest
import shopping.member.application.MemberService

abstract class LoggedInE2ETest : E2ETest() {
    @Autowired
    private lateinit var memberService: MemberService
    protected lateinit var accessToken: String

    @BeforeEach
    override fun setup() {
        super.setup()
        setAccessToken()
    }

    private fun setAccessToken() {
        this.accessToken =
            memberService
                .regist(
                    MemberRegistRequest(
                        email = "test@test.com",
                        password = "testpassword12",
                        name = "tester",
                    ),
                ).accessToken
    }
}
