package shopping.shop.application;

import shopping.shop.application.command.ShopRegistrationCommand;
import shopping.shop.application.query.ShopRegistrationQuery;

public interface ShopRegistrationUseCase {
    ShopRegistrationQuery register(final ShopRegistrationCommand shopRegistrationCommand);
}
