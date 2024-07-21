package shopping.shop.domain;

public interface ShopRepository {
    Shop save(final ShopRegistrationRequest shopRegistrationRequest);
}
