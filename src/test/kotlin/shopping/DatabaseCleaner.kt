package shopping

import jakarta.persistence.EntityManager
import jakarta.persistence.Table
import jakarta.persistence.metamodel.EntityType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DatabaseCleaner(
    private val entityManager: EntityManager,
) {
    private val tableNames: List<String> by lazy {
        entityManager.metamodel.entities.map { resolveTableName(it) }
    }

    private fun resolveTableName(entityType: EntityType<*>): String {
        val tableAnnotation = entityType.javaType.getAnnotation(Table::class.java)

        if (tableAnnotation != null && tableAnnotation.name.isNotBlank()) {
            return tableAnnotation.name
        }

        return entityType.name
            .replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
            .lowercase()
    }

    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate()

        for (tableName in tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
        }

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate()
    }
}
