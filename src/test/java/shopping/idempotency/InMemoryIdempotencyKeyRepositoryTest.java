package shopping.idempotency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryIdempotencyKeyRepositoryTest {

    private InMemoryIdempotencyKeyRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIdempotencyKeyRepository();
    }

    @Test
    void 새로운_키를_삽입하면_빈_값을_반환한다() {
        IdempotencyKey key = new IdempotencyKey("key-1");

        Optional<IdempotencyKey> result = repository.putIfAbsent(key);

        assertTrue(result.isEmpty());
    }

    @Test
    void 이미_존재하는_키를_삽입하면_기존_키를_반환한다() {
        IdempotencyKey first = new IdempotencyKey("key-1");
        repository.putIfAbsent(first);

        IdempotencyKey second = new IdempotencyKey("key-1");
        Optional<IdempotencyKey> result = repository.putIfAbsent(second);

        assertTrue(result.isPresent());
        assertEquals(first, result.get());
    }

    @Test
    void 키를_삭제하면_다시_삽입할_수_있다() {
        IdempotencyKey key = new IdempotencyKey("key-1");
        repository.putIfAbsent(key);

        repository.remove("key-1");

        IdempotencyKey newKey = new IdempotencyKey("key-1");
        Optional<IdempotencyKey> result = repository.putIfAbsent(newKey);
        assertTrue(result.isEmpty());
    }

    @Test
    void 완료된_키를_조회하면_COMPLETED_상태이다() {
        IdempotencyKey key = new IdempotencyKey("key-1");
        repository.putIfAbsent(key);
        key.complete();
        repository.save(key);

        IdempotencyKey existing = repository.putIfAbsent(new IdempotencyKey("key-1")).orElseThrow();
        assertEquals(IdempotencyKey.Status.COMPLETED, existing.getStatus());
    }
}
