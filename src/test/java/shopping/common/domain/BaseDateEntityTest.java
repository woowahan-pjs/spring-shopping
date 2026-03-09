package shopping.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[공통] 기본 날짜 엔티티 단위 테스트")
class BaseDateEntityTest {
    @Test
    @DisplayName("생성 시 createdAt과 updatedAt을 함께 채운다")
    void prePersistSetCreatedAtAndUpdatedAt() {
        TestEntity entity = new TestEntity();

        entity.callPrePersist();

        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isEqualTo(entity.getCreatedAt());
    }

    @Test
    @DisplayName("수정 시 updatedAt을 다시 채운다")
    void preUpdateRefreshUpdatedAt() {
        TestEntity entity = new TestEntity();
        entity.callPrePersist();
        LocalDateTime previousUpdatedAt = entity.getUpdatedAt();

        entity.callPreUpdate();

        assertThat(entity.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    @DisplayName("삭제 표시를 하면 deletedAt을 채운다")
    void markDeletedSetDeletedAt() {
        TestEntity entity = new TestEntity();

        entity.callMarkDeleted();

        assertThat(entity.getDeletedAt()).isNotNull();
    }

    static class TestEntity extends BaseDateEntity {
        void callPrePersist() {
            prePersist();
        }

        void callPreUpdate() {
            preUpdate();
        }

        void callMarkDeleted() {
            markDeleted();
        }
    }
}
