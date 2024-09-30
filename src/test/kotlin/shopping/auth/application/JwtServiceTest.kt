package shopping.auth.application

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.springframework.security.core.userdetails.UserDetails
import shopping.global.exception.ApplicationException
import shopping.member.domain.Member
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@DisplayName("JwtService 테스트")
class JwtServiceTest : BehaviorSpec({
    val tokenProvider: TokenProvider = mockk()
    val jwtProperties: JwtProperties = mockk(relaxed = true)
    val objectMapper: ObjectMapper = mockk()

    val jwtService = JwtService(tokenProvider, jwtProperties, objectMapper)

    Given("토큰을 받아 username 을 추출 하여 반환할 때") {
        val token = "token"

        When("username 을 정상적으로 추출 했다면") {
            every { tokenProvider.extractUsername(token) } returns "username"

            val actual = jwtService.getUsername(token)

            Then("username 을 반환 한다") {
                actual.shouldNotBeBlank()
            }
        }

        When("username 이 null 이면") {
            every { tokenProvider.extractUsername(token) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    jwtService.getUsername(token)
                } shouldHaveMessage "CLAIM 정보가 비어있습니다."
            }
        }

        When("username 이 공백 이면") {
            every { tokenProvider.extractUsername(token) } returns ""

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    jwtService.getUsername(token)
                } shouldHaveMessage "CLAIM 정보가 비어있습니다."
            }
        }
    }

    Given("토큰을 받아 jti 를 추출 하여 반환할 때") {
        val token = "token"

        When("jti 를 정상적으로 추출 했다면") {
            every { tokenProvider.extractJti(token) } returns "jti"

            val actual = jwtService.getJti(token)

            Then("jti 를 반환 한다") {
                actual shouldBe "jti"
            }
        }

        When("jti 가 null 이면") {
            every { tokenProvider.extractJti(token) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    jwtService.getJti(token)
                } shouldHaveMessage "CLAIM 정보가 비어있습니다."
            }
        }

        When("jti 가 공백 이면") {
            every { tokenProvider.extractJti(token) } returns ""

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    jwtService.getJti(token)
                } shouldHaveMessage "CLAIM 정보가 비어있습니다."
            }
        }
    }

    Given("userDetails 와 jti 를 받아") {
        val userDetails: UserDetails = mockk()
        val jti = "jti"

        every { tokenProvider.buildToken(userDetails, jti, any<Long>()) } returns "token"

        When("accessToken 을 생성 후") {
            val actual = jwtService.generateAccessToken(userDetails, jti)

            Then("accessToken 을 반환 한다") {
                actual.shouldNotBeBlank()
            }
        }

        When("refreshToken 을 생성 후") {
            val actual = jwtService.generateRefreshToken(userDetails, jti)

            Then("refreshToken 을 반환 한다") {
                actual.shouldNotBeBlank()
            }
        }
    }

    Given("userDetails 를 받아") {
        val member: Member = mockk()

        every { tokenProvider.buildToken(member, any(), any()) } returns "token"
        every { member.id } returns 1L

        When("토큰 쌍을 생성 후") {
            val actual = jwtService.generateAuthenticationCredentials(member)

            Then("반환 한다") {
                actual.shouldNotBeNull()
            }
        }
    }

    Given("토큰이 유효 한지 검증할 때") {
        val token = "token"
        val userDetails: UserDetails = mockk()
        val username = "username"
        val expiration: Date = mockk()

        When("정상적인 토큰 이라면") {
            every { tokenProvider.extractUsername(token) } returns username
            every { userDetails.username } returns username
            every { tokenProvider.extractExpiration(token) } returns expiration
            every { expiration.after(any<Date>()) } returns true

            val actual = jwtService.isTokenValid(token, userDetails)

            Then("true 를 반환 한다") {
                actual.shouldBeTrue()
            }
        }

        When("claims 중 username 과 userDetails 의 username 이 다르다면") {
            val username1 = "username1"
            val username2 = "username2"
            every { tokenProvider.extractUsername(token) } returns username1
            every { userDetails.username } returns username2

            val actual = jwtService.isTokenValid(token, userDetails)

            Then("false 를 반환 한다") {
                actual.shouldBeFalse()
            }
        }

        When("claims 중 만료 시간이 지났다면") {
            every { tokenProvider.extractUsername(token) } returns username
            every { userDetails.username } returns username
            every { tokenProvider.extractExpiration(token) } returns expiration
            every { expiration.after(any<Date>()) } returns false

            val actual = jwtService.isTokenValid(token, userDetails)

            Then("false 를 반환 한다") {
                actual.shouldBeFalse()
            }
        }

        When("claims 중 만료 시간이 null 이라면") {
            every { tokenProvider.extractUsername(token) } returns username
            every { userDetails.username } returns username
            every { tokenProvider.extractExpiration(token) } returns null

            val actual = jwtService.isTokenValid(token, userDetails)

            Then("false 를 반환 한다") {
                actual.shouldBeFalse()
            }
        }
    }

    Given("토큰 문자열을 이용해 만료 시간을 체크할 때") {
        val token = "token.token.token"
        val decoder: Base64.Decoder = mockk(relaxed = true)
        val now: ZonedDateTime = mockk(relaxed = true)
        val dateTimeInstance: Instant = mockk()
        val readObject = mutableMapOf("exp" to "1000")
        mockkStatic(Base64::class)
        mockkStatic(ZonedDateTime::class)

        every { objectMapper.readValue(any<String>(), any<TypeReference<MutableMap<String, String>>>()) } returns readObject
        every { Base64.getDecoder() } returns decoder
        every { ZonedDateTime.now() } returns now
        every { now.toInstant() } returns dateTimeInstance

        When("만료 시간이 아직 지나지 않았다면") {
            val currentMillis = 900L
            every { dateTimeInstance.toEpochMilli() } returns currentMillis

            val actual = jwtService.checkTokenExpiredByTokenString(token)

            Then("false 를 반환 한다") {
                actual.shouldBeFalse()
            }
        }

        When("만료 시간이 지났다면") {
            val currentMillis = 900_000_000L
            every { dateTimeInstance.toEpochMilli() } returns currentMillis

            val actual = jwtService.checkTokenExpiredByTokenString(token)

            Then("true 를 반환 한다") {
                actual.shouldBeTrue()
            }
        }

        When("토큰에 만료 시간이 없다면") {
            val emptyPayload = mutableMapOf("" to "")
            every { objectMapper.readValue(any<String>(), any<TypeReference<MutableMap<String, String>>>()) } returns emptyPayload

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    jwtService.checkTokenExpiredByTokenString(token)
                } shouldHaveMessage "액세스 토큰이 유효하지 않습니다."
            }
        }
    }
})
