package shopping.seller.application;

import org.springframework.stereotype.Service;
import shopping.seller.application.command.SellerSignUpCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.domain.SellerSignUpRequest;

@Service
public class SellerSignUpService implements SellerSignUpUseCase {
    private final SellerRepository sellerRepository;

    public SellerSignUpService(final SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller signUp(final SellerSignUpCommand sellerSignUpCommand) {
        return sellerRepository.save(mapToDomain(sellerSignUpCommand));
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
