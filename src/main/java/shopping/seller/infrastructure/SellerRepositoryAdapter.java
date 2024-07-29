package shopping.seller.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.infrastructure.persistence.SellerEntity;
import shopping.seller.infrastructure.persistence.SellerEntityJpaRepository;

import static shopping.seller.infrastructure.SellerEntityMapper.domainToEntity;
import static shopping.seller.infrastructure.SellerEntityMapper.entityToDomain;

@Component
public class SellerRepositoryAdapter implements SellerRepository {

    private final SellerEntityJpaRepository repository;

    public SellerRepositoryAdapter(final SellerEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Seller save(final Seller seller) {
        final SellerEntity sellerEntity = repository.save(domainToEntity(seller));
        return entityToDomain(sellerEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Seller findByEmail(final String email) {
        return repository.findByEmail(email)
                .map(SellerEntityMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
