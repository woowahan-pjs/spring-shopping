package shopping.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("[공통] 기본 날짜 엔티티 JPA 테스트")
class BaseDateEntityTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("저장 시 createdAt과 updatedAt을 함께 채운다")
    void persistSetCreatedAtAndUpdatedAt() {
        AuditedEntity entity = new AuditedEntity("first");

        AuditedEntity saved = entityManager.persistFlushFind(entity);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isEqualTo(saved.getCreatedAt());
    }

    @Test
    @DisplayName("수정 시 updatedAt을 다시 채운다")
    void updateRefreshUpdatedAt() {
        AuditedEntity entity = entityManager.persistFlushFind(new AuditedEntity("first"));
        LocalDateTime previousUpdatedAt = entity.getUpdatedAt();

        entity.rename("second");
        entityManager.flush();
        entityManager.clear();

        AuditedEntity updated = entityManager.find(AuditedEntity.class, entity.getId());
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    @DisplayName("삭제 표시를 하면 deletedAt을 채운다")
    void markDeletedSetDeletedAt() {
        AuditedEntity entity = new AuditedEntity("first");

        entity.markAsDeleted();

        assertThat(entity.getDeletedAt()).isNotNull();
    }

    @Entity
    @Table(name = "audited_entity")
    static class AuditedEntity extends BaseDateEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        protected AuditedEntity() {
        }

        AuditedEntity(String name) {
            this.name = name;
        }

        Long getId() {
            return id;
        }

        void rename(String name) {
            this.name = name;
        }

        void markAsDeleted() {
            markDeleted();
        }
    }
}
