package shopping.shop.application;

import org.springframework.stereotype.Service;
import shopping.shop.application.command.ShopRegistrationCommand;
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
    public Shop register(final ShopRegistrationCommand shopRegistrationCommand) {
        return shopRepository.save(mapToDomain(shopRegistrationCommand));
    }

    private ShopRegistrationRequest mapToDomain(final ShopRegistrationCommand shopRegistrationCommand) {
        return new ShopRegistrationRequest(
                shopRegistrationCommand.name(),
                shopRegistrationCommand.userId()
        );
    }
}