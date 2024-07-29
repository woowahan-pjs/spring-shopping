package shopping.utils.fake;

import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeSellerRepository implements SellerRepository {
    private final Map<Long, Seller> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Seller save(final Seller sellerRegistration) {
        final boolean exist = storage.values().stream()
                .map(Seller::email)
                .anyMatch(it -> it.equals(sellerRegistration.email()));
        if (exist) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        final var id = idGenerator.incrementAndGet();

        final Seller seller = new Seller(
                id,
                sellerRegistration.email(),
                sellerRegistration.name(),
                sellerRegistration.password(),
                sellerRegistration.birth(),
                sellerRegistration.address(),
                sellerRegistration.phone()
        );
        storage.put(id, sellerRegistration);
        return seller;
    }

    @Override
    public Seller findByEmail(final String email) {
        return storage.values().stream().filter(it -> it.email().equals(email))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}