package shopping.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.HttpStatus
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import shopping.auth.application.AuthenticationCommandService
import shopping.auth.application.JwtAuthenticationFilter
import shopping.auth.application.JwtService
import shopping.auth.application.TokenQueryRepository
import shopping.auth.presentation.AuthenticationApi
import shopping.global.config.SecurityConfig
import shopping.global.exception.ErrorCode
import shopping.member.application.MemberCommandService
import shopping.member.application.MemberQueryRepository
import shopping.member.application.MemberQueryService
import shopping.member.presentation.MemberApi
import shopping.product.application.ProductCommandService
import shopping.product.application.ProductQueryService
import shopping.product.presentation.ProductApi

@WebMvcTest(
    controllers = [
        MemberApi::class,
        AuthenticationApi::class,
        ProductApi::class,
    ],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                JwtAuthenticationFilter::class,
                SecurityConfig::class,
            ],
        ),
    ],
)
@MockkBean(JpaMetamodelMappingContext::class)
abstract class KotestControllerTestSupport : BehaviorSpec() {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var h2ConsoleProperties: H2ConsoleProperties

    @MockkBean
    private lateinit var memberQueryRepository: MemberQueryRepository

    @MockkBean
    private lateinit var jwtService: JwtService

    @MockkBean
    private lateinit var logoutHandler: LogoutHandler

    @MockkBean
    private lateinit var entryPoint: AuthenticationEntryPoint

    @MockkBean
    private lateinit var accessDeniedHandler: AccessDeniedHandler

    @MockkBean
    private lateinit var tokenRepository: TokenQueryRepository

    @MockkBean
    protected lateinit var memberCommandService: MemberCommandService

    @MockkBean
    protected lateinit var memberQueryService: MemberQueryService

    @MockkBean
    protected lateinit var authenticationCommandService: AuthenticationCommandService

    @MockkBean
    protected lateinit var productCommandService: ProductCommandService

    @MockkBean
    protected lateinit var productQueryService: ProductQueryService

    init {
        afterContainer {
            clearAllMocks()
        }
    }

    override fun extensions(): List<Extension> = listOf(SpringExtension)

    protected fun ResultActions.isStatusAs(status: HttpStatus): ResultActions =
        this.andExpectAll(
            MockMvcResultMatchers.status().`is`(status.value()),
            MockMvcResultMatchers.jsonPath("$.meta.code").value(status.value()),
            MockMvcResultMatchers.jsonPath("$.meta.message").value(status.reasonPhrase),
        )

    protected fun ResultActions.isInvalidInputValueResponse(message: String): ResultActions =
        this.andExpect(MockMvcResultMatchers.jsonPath("$.meta.code").value(ErrorCode.INVALID_INPUT_VALUE.status.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.message").value(message))

    protected fun ResultActionsDsl.isInvalidInputValueResponse(message: String): ResultActionsDsl =
        this.andExpectAll {
            jsonPath("$.meta.code") { value(ErrorCode.INVALID_INPUT_VALUE.status.value()) }
            jsonPath("$.meta.message") { value(message) }
        }
}
