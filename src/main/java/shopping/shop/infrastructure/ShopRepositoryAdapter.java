package shopping.shop.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.shop.domain.Shop;
import shopping.shop.domain.ShopRepository;
import shopping.shop.infrastructure.pesistence.ShopEntity;
import shopping.shop.infrastructure.pesistence.ShopEntityRepository;

import static shopping.shop.infrastructure.ShopEntityMapper.domainToEntity;
import static shopping.shop.infrastructure.ShopEntityMapper.entityToDomain;

@Component
public class ShopRepositoryAdapter implements ShopRepository {

    private final ShopEntityRepository shopEntityRepository;

    public ShopRepositoryAdapter(final ShopEntityRepository shopEntityRepository) {
        this.shopEntityRepository = shopEntityRepository;
    }

    @Transactional
    @Override
    public Shop save(final Shop shop) {
        final ShopEntity shopEntity = shopEntityRepository.save(domainToEntity(shop));
        return entityToDomain(shopEntity);
    }
}
