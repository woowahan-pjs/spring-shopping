package shopping.product.domain;

import org.springframework.stereotype.Component;
import shopping.common.exception.ProductNameInvalidException;
import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.domain.clients.ProfanityClient;

@Component
public class ProductValidator {
    private final ProfanityClient profanityClient;

    public ProductValidator(final ProfanityClient profanityClient) {
        this.profanityClient = profanityClient;
    }

    public void verify(final ProductRegistrationCommand command) {
        if (profanityClient.containProfanity(command.name())) {
            throw new ProductNameInvalidException();
        }
    }
}
