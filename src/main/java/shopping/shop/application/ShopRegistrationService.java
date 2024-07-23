package shopping.shop.application;

import org.springframework.stereotype.Service;
import shopping.shop.application.command.ShopRegistrationCommand;
import shopping.shop.application.query.ShopRegistrationQuery;
import shopping.shop.domain.Shop;
import shopping.shop.domain.ShopRegistrationRequest;
import shopping.shop.domain.ShopRepository;

@Service
public class ShopRegistrationService implements ShopRegistrationUseCase {

    private final ShopRepository shopRepository;

    public ShopRegistrationService(final ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public ShopRegistrationQuery register(final ShopRegistrationCommand shopRegistrationCommand) {
        final Shop shop = shopRepository.save(mapToDomain(shopRegistrationCommand));
        return new ShopRegistrationQuery(shop);
    }

    private ShopRegistrationRequest mapToDomain(final ShopRegistrationCommand shopRegistrationCommand) {
        return new ShopRegistrationRequest(
                shopRegistrationCommand.name(),
                shopRegistrationCommand.userId()
        );
    }
}