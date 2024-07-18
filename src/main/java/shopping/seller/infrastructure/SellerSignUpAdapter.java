package shopping.seller.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.domain.SellerSignUpRequest;

@Component
public class SellerSignUpAdapter implements SellerRepository {

    private final SellerEntityJpaRepository repository;

    public SellerSignUpAdapter(final SellerEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Seller save(final SellerSignUpRequest sellerSignUpRequest) {
        final SellerEntity sellerEntity = repository.save(domainToEntity(sellerSignUpRequest));
        return entityToDomain(sellerEntity);
    }

    @Override
    public Seller findByEmail(final String email) {
        final SellerEntity sellerEntity = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException());
        return entityToDomain(sellerEntity);
    }

    private SellerEntity domainToEntity(final SellerSignUpRequest sellerSignUpRequest) {
        return new SellerEntity(
                sellerSignUpRequest.email(),
                sellerSignUpRequest.name(),
                sellerSignUpRequest.address(),
                sellerSignUpRequest.birth(),
                sellerSignUpRequest.phone(),
                sellerSignUpRequest.password());
    }

    private Seller entityToDomain(final SellerEntity sellerEntity) {
        return new Seller(
                sellerEntity.getId(),
                sellerEntity.getEmail(),
                sellerEntity.getName(),
                sellerEntity.getPassword(),
                sellerEntity.getBirth(),
                sellerEntity.getAddress(),
                sellerEntity.getPhone()
        );
    }
}
