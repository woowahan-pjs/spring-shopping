package shopping.idempotency;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class IdempotencyKeyTest {

    @Test
    void 생성하면_PENDING_상태이다() {
        IdempotencyKey key = new IdempotencyKey("key-1");

        assertEquals(IdempotencyKey.Status.PENDING, key.getStatus());
    }

    @Test
    void 완료하면_COMPLETED_상태이다() {
        IdempotencyKey key = new IdempotencyKey("key-1");

        key.complete();

        assertEquals(IdempotencyKey.Status.COMPLETED, key.getStatus());
    }
}
