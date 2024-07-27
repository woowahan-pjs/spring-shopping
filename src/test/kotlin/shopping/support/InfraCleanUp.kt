package shopping.support

import jakarta.persistence.EntityManager
import jakarta.persistence.Table
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InfraCleanUp(
    private var jdbcTemplate: JdbcTemplate,
    private var entityManager: EntityManager,
) {
    @Transactional
    fun all() {
        val tables =
            entityManager.metamodel.entities.map { entityType ->
                val tableName = entityType.javaType.getAnnotation(Table::class.java)?.name

                if (tableName.isNullOrBlank()) {
                    return@map entityType.name
                }

                return@map tableName
            }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE;")
        tables.forEach { table -> jdbcTemplate.execute("TRUNCATE table $table") }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;")
    }
}
