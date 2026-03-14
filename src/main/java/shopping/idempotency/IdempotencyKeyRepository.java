package shopping.idempotency;

import java.util.Optional;

public interface IdempotencyKeyRepository {

    /**
     * Atomically inserts a new key only if absent. Returns the existing key if already present, or
     * empty if the new key was inserted.
     */
    Optional<IdempotencyKey> putIfAbsent(IdempotencyKey idempotencyKey);

    void save(IdempotencyKey idempotencyKey);

    void remove(String key);
}
