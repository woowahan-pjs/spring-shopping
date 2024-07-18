package shopping.seller.domain;

public interface SellerRepository {
    Seller save(final SellerSignUpRequest sellerSignUpRequest);
}
