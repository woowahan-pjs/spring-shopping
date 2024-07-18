package shopping.seller.application;

import org.springframework.stereotype.Service;
import shopping.common.exception.PasswordMissMatchException;
import shopping.customer.domain.AccessToken;
import shopping.seller.application.command.SellerSignInCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;

@Service
public class SellerSignInService implements SellerSignInUseCase {
    private final SellerRepository sellerRepository;

    public SellerSignInService(final SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public AccessToken signIn(final SellerSignInCommand sellerSignInCommand) {
        final Seller seller = sellerRepository.findByEmail(sellerSignInCommand.email());
        if (seller.isSamePassword(sellerSignInCommand.password())) {
            return new AccessToken("1234");
        }
        throw new PasswordMissMatchException();
    }
}
