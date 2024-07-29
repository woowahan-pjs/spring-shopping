package shopping.seller.application;

import shopping.seller.application.command.SellerSignInCommand;

public interface SellerSignInUseCase {
    String signIn(final SellerSignInCommand sellerSignInCommand);
}
