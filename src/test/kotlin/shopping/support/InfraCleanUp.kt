package shopping.support

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InfraCleanUp(
    private var jdbcTemplate: JdbcTemplate,
) {
    @Transactional
    fun all() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")
        cleanUpTables()
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;")
    }

    private fun cleanUpTables() = jdbcTemplate.queryForList(
        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
        String::class.java).forEach(::cleanUpTable)

    private fun cleanUpTable(tableName: String) = jdbcTemplate.execute("TRUNCATE table $tableName RESTART IDENTITY")
}
