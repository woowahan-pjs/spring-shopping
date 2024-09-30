package shopping.global.infra

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import shopping.support.KotestIntegrationTestSupport

@DisplayName("TransactionTemplate 통합 테스트 - 트랜잭션 내부 로직에선 트랜잭션 설정이 없는 케이스에 대한 검증")
class TransactionTemplateIntegrationTest : KotestIntegrationTestSupport() {

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    init {

        beforeSpec {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test_table (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))")
        }

        Given("트랜잭션 내부 로직을 실행할 때") {
            val value = "test"

            When("로직 내부에서 예외가 발생 하면") {
                runCatching {
                    transactionTemplate.execute {
                        jdbcTemplate.update("INSERT INTO test_table (name) VALUES ('$value')")

                        throw RuntimeException()
                    }
                }

                Then("트랜잭션을 롤백 한다") {
                    val actual = getRowCount()

                    actual shouldBe 0
                }
            }

            When("로직 내부에서 예외가 발생 하지 않으면") {
                val actual = transactionTemplate.execute {
                    jdbcTemplate.update("INSERT INTO test_table (name) VALUES ('$value')")
                    "test"
                }

                Then("트랜잭션을 커밋 한다") {
                    val count = getRowCount()

                    count shouldBe 1
                }

                Then("정의된 값을 반환 한다") {
                    actual shouldBe "test"
                }
            }
        }
    }

    private fun getRowCount(): Int? {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_table", Int::class.java)
    }
}

