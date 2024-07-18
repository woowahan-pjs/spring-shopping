package shopping.seller.application;

import shopping.customer.domain.AccessToken;
import shopping.seller.application.command.SellerSignInCommand;

public interface SellerSignInUseCase {
    AccessToken signIn(final SellerSignInCommand sellerSignInCommand);
}
