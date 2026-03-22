package shopping.infrastructure.product.client

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [
        ProductBadWordClientAdapterSpringTest.FakeProfanityController::class,
        ServletWebServerFactoryAutoConfiguration::class,
        DispatcherServletAutoConfiguration::class,
        WebMvcAutoConfiguration::class,
        JacksonAutoConfiguration::class,
    ],
)
class ProductBadWordClientAdapterSpringTest(
    @LocalServerPort private val port: Int,
) : BehaviorSpec({

        extension(SpringExtension)

        // 테스트 대상 어댑터를 직접 생성 (Spring 빈으로 관리할 필요 없음)
        val webClient =
            WebClient
                .builder()
                .baseUrl("http://localhost:$port")
                .build()
        val productBadWordClientAdapter = ProductBadWordClientAdapter(webClient)

        given("비속어 확인을 위해 Spring 서버(Fake Controller)가 실행 중일 때") {

            `when`("텍스트에 '시발'이 포함되어 있으면") {
                val text = "시발"
                val result = productBadWordClientAdapter.containsProfanity(text)

                then("Fake Controller가 true를 응답한다") {
                    result shouldBe true
                }
            }

            `when`("텍스트에 비속어가 없으면 (예: '안녕')") {
                val text = "안녕"
                val result = productBadWordClientAdapter.containsProfanity(text)

                then("Fake Controller가 false를 응답한다") {
                    result shouldBe false
                }
            }

            `when`("서버에서 에러를 반환하도록 요청하면 (예: 'error')") {
                val text = "error"
                val result = productBadWordClientAdapter.containsProfanity(text)

                then("어댑터는 false를 반환하며 예외를 삼킨다 (block ?: false 처리)") {
                    result shouldBe false
                }
            }
        }
    }) {
    @RestController
    class FakeProfanityController {
        @GetMapping("/service/containsprofanity")
        fun containsProfanity(
            @RequestParam text: String,
        ): ResponseEntity<Boolean> =
            when (text) {
                "시발" -> ResponseEntity.ok(true)
                "error" -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                else -> ResponseEntity.ok(false)
            }
    }
}
