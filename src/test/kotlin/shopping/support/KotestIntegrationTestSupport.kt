package shopping.support

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class KotestIntegrationTestSupport : BehaviorSpec() {
    @LocalServerPort
    protected var port: Int = -1

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var cleanUp: InfraCleanUp

    init {
        beforeContainer {
            clearAllMocks()
        }

        afterEach {
            cleanUp.all()
        }

        afterContainer {
            clearAllMocks()
        }
    }

    override fun extensions(): List<Extension> = listOf(SpringExtension)
}
