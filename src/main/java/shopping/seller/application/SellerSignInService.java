package shopping.seller.application;

import org.springframework.stereotype.Service;
import shopping.auth.AccessTokenRepository;
import shopping.auth.AuthorizationType;
import shopping.common.exception.PasswordMissMatchException;
import shopping.seller.application.command.SellerSignInCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;

@Service
public class SellerSignInService implements SellerSignInUseCase {
    private final AccessTokenRepository accessTokenRepository;
    private final SellerRepository sellerRepository;

    public SellerSignInService(final AccessTokenRepository accessTokenRepository, final SellerRepository sellerRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public String signIn(final SellerSignInCommand sellerSignInCommand) {
        final Seller seller = sellerRepository.findByEmail(sellerSignInCommand.email());
        if (seller.isSamePassword(sellerSignInCommand.password())) {
            return accessTokenRepository.create(AuthorizationType.SELLER, seller.id());
        }
        throw new PasswordMissMatchException();
    }
}
