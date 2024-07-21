package shopping.shop.infrastructure;

import org.springframework.stereotype.Component;
import shopping.shop.domain.Shop;
import shopping.shop.domain.ShopRegistrationRequest;
import shopping.shop.domain.ShopRepository;
import shopping.shop.infrastructure.pesistence.ShopEntity;
import shopping.shop.infrastructure.pesistence.ShopEntityRepository;

@Component
public class ShopRepositoryAdapter implements ShopRepository {

    private final ShopEntityRepository shopEntityRepository;

    public ShopRepositoryAdapter(final ShopEntityRepository shopEntityRepository) {
        this.shopEntityRepository = shopEntityRepository;
    }

    @Override
    public Shop save(final ShopRegistrationRequest shopRegistrationRequest) {
        final ShopEntity shopEntity = shopEntityRepository.save(new ShopEntity(shopRegistrationRequest.userId(), shopRegistrationRequest.name()));
        return mapToDomain(shopEntity);
    }

    public Shop mapToDomain(final ShopEntity shopEntity) {
        return new Shop(shopEntity.getId(), shopEntity.getSellerId(), shopEntity.getName());
    }
}
