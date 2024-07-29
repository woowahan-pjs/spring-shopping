package shopping

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import shopping.common.util.toSnakeCase

@Component
class DatabaseCleaner(
    @PersistenceContext
    private val entityManager: EntityManager,
) {
    @Transactional
    fun clean() {
        entityManager.clear()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        getTableNames().forEach {
            val tableName = it.name.toSnakeCase().lowercase()
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }

    private fun getTableNames() =
        entityManager
            .metamodel
            .entities
            .filter { it.javaType.isAnnotationPresent(Entity::class.java) }
}
