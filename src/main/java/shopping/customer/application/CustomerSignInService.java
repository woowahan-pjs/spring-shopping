package shopping.customer.application;

import org.springframework.stereotype.Service;
import shopping.auth.AccessTokenRepository;
import shopping.auth.AuthorizationType;
import shopping.common.exception.PasswordMissMatchException;
import shopping.customer.application.command.CustomerSignInCommand;
import shopping.customer.domain.Customer;
import shopping.customer.domain.repository.CustomerRepository;

@Service
public class CustomerSignInService implements CustomerSignInUseCase {

    private final AccessTokenRepository accessTokenRepository;
    private final CustomerRepository customerRepository;

    public CustomerSignInService(final AccessTokenRepository accessTokenRepository, final CustomerRepository customerRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String signIn(final CustomerSignInCommand customerSignInCommand) {
        final Customer customer = customerRepository.findByEmail(customerSignInCommand.email());
        if (customer.isSamePassword(customerSignInCommand.password())) {
            return accessTokenRepository.create(AuthorizationType.CUSTOMER, customer.id());
        }
        throw new PasswordMissMatchException();
    }
}
