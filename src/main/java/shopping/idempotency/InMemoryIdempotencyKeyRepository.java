package shopping.idempotency;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryIdempotencyKeyRepository implements IdempotencyKeyRepository {

    private final ConcurrentHashMap<String, IdempotencyKey> store = new ConcurrentHashMap<>();

    @Override
    public Optional<IdempotencyKey> putIfAbsent(IdempotencyKey idempotencyKey) {
        IdempotencyKey existing = store.putIfAbsent(idempotencyKey.getKey(), idempotencyKey);
        return Optional.ofNullable(existing);
    }

    @Override
    public void save(IdempotencyKey idempotencyKey) {
        store.put(idempotencyKey.getKey(), idempotencyKey);
    }

    @Override
    public void remove(String key) {
        store.remove(key);
    }
}
