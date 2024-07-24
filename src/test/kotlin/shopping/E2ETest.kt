package shopping

import io.restassured.RestAssured
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
abstract class E2ETest {
    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
        RestAssured.baseURI = "http://localhost"
    }
}
