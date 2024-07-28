package shopping.seller.domain;

public interface SellerRepository {
    Seller save(final Seller seller);

    Seller findByEmail(String email);
}
