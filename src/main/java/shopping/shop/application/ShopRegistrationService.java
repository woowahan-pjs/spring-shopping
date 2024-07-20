package shopping.shop.application;

import org.springframework.stereotype.Service;
import shopping.shop.application.command.ShopRegistrationCommand;
import shopping.shop.domain.Shop;

@Service
public class ShopRegistrationService implements ShopRegistrationUseCase {

    @Override
    public Shop register(ShopRegistrationCommand shopRegistrationCommand) {
        return new Shop();
    }
}
