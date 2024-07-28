package shopping.seller.application;

import org.springframework.stereotype.Service;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.auth.AuthorizationType;
import shopping.common.exception.PasswordMissMatchException;
import shopping.seller.application.command.SellerSignInCommand;
import shopping.seller.application.command.SellerSignUpCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.domain.SellerSignUpRequest;

@Service
public class SellerService implements SellerSignInUseCase, SellerSignUpUseCase, SellerSignOutUseCase {
    private final AccessTokenRepository accessTokenRepository;
    private final SellerRepository sellerRepository;

    public SellerService(final AccessTokenRepository accessTokenRepository, final SellerRepository sellerRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller signUp(final SellerSignUpCommand sellerSignUpCommand) {
        return sellerRepository.save(mapToDomain(sellerSignUpCommand));
    }

    @Override
    public String signIn(final SellerSignInCommand sellerSignInCommand) {
        final Seller seller = sellerRepository.findByEmail(sellerSignInCommand.email());
        if (seller.isSamePassword(sellerSignInCommand.password())) {
            return accessTokenRepository.create(AuthorizationType.SELLER, seller.id());
        }
        throw new PasswordMissMatchException();
    }

    @Override
    public void signOut(final long userId) {
        accessTokenRepository.delete(AuthorizationType.SELLER, userId);
    }

    private SellerSignUpRequest mapToDomain(final SellerSignUpCommand sellerSignUpCommand) {
        return new SellerSignUpRequest(
                sellerSignUpCommand.email(),
                sellerSignUpCommand.name(),
                sellerSignUpCommand.password(),
                sellerSignUpCommand.birth(),
                sellerSignUpCommand.address(),
                sellerSignUpCommand.phone()
        );
    }
}
