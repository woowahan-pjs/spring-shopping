package shopping.core;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@TestComponent
public class DatabaseCleaner {

    private final EntityManager entityManager;
    private final List<String> tableNames;

    public DatabaseCleaner(final EntityManager entityManager) {
        this.entityManager = entityManager;
        this.tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().isAnnotationPresent(Entity.class))
                .map(EntityType::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.clear();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (final String tableName : tableNames) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s",tableName)).executeUpdate();
            entityManager.createNativeQuery(String.format("ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1",tableName)).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
