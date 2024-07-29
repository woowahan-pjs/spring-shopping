package shopping.seller.application;

import shopping.seller.application.command.SellerSignUpCommand;
import shopping.seller.domain.Seller;

public interface SellerSignUpUseCase {
    Seller signUp(SellerSignUpCommand sellerSignUpCommand);
}
