package shopping.shop.application;

import shopping.shop.application.command.ShopRegistrationCommand;
import shopping.shop.domain.Shop;

public interface ShopRegistrationUseCase {
    Shop register(final ShopRegistrationCommand shopRegistrationCommand);
}
