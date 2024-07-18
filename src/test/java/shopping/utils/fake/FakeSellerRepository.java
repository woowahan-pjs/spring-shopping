package shopping.utils.fake;

import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.domain.SellerSignUpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeSellerRepository implements SellerRepository {
    private final Map<Long, Seller> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Seller save(final SellerSignUpRequest sellerSignUpRequest) {
        final boolean exist = storage.values().stream()
                .map(Seller::getEmail)
                .anyMatch(it -> it.equals(sellerSignUpRequest.email()));
        if (exist) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        final var id = idGenerator.incrementAndGet();
        final Seller seller = new Seller(
                id,
                sellerSignUpRequest.email(),
                sellerSignUpRequest.name(),
                sellerSignUpRequest.password(),
                sellerSignUpRequest.birth(),
                sellerSignUpRequest.address(),
                sellerSignUpRequest.phone()
        );
        storage.put(id, seller);
        return seller;
    }
}