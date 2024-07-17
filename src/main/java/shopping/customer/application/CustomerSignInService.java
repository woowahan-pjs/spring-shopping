package shopping.customer.application;

import org.springframework.stereotype.Service;
import shopping.customer.application.command.CustomerSignInCommand;
import shopping.customer.domain.AccessToken;
import shopping.customer.domain.Customer;
import shopping.customer.domain.repository.CustomerRepository;

@Service
class CustomerSignInService implements CustomerSignInUseCase {

    private final CustomerRepository customerRepository;

    public CustomerSignInService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public AccessToken signIn(final CustomerSignInCommand customerSignInCommand) {
        final Customer customer = customerRepository.findByEmail(customerSignInCommand.email());
        if (customer.isSamePassword(customerSignInCommand.password())) {
            return new AccessToken("1234");
        }
        throw new RuntimeException();
    }
}
