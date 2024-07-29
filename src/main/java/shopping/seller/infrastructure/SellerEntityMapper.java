package shopping.seller.infrastructure;

import shopping.seller.domain.Seller;
import shopping.seller.infrastructure.persistence.SellerEntity;

public class SellerEntityMapper {
    private SellerEntityMapper() {
    }

    public static SellerEntity domainToEntity(final Seller seller) {
        return new SellerEntity(
                seller.id(),
                seller.email(),
                seller.name(),
                seller.address(),
                seller.birth(),
                seller.phone(),
                seller.password());
    }

    public static Seller entityToDomain(final SellerEntity sellerEntity) {
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
