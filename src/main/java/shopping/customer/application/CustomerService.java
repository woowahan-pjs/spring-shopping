package shopping.customer.application;

import org.springframework.stereotype.Service;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.auth.AuthorizationType;
import shopping.common.exception.PasswordMissMatchException;
import shopping.customer.application.command.CustomerSignInCommand;
import shopping.customer.application.command.CustomerSignUpCommand;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;

@Service
public class CustomerService implements CustomerSignUpUseCase, CustomerSignInUseCase, CustomerSignOutUseCase {

    private final AccessTokenRepository accessTokenRepository;
    private final CustomerRepository customerRepository;

    public CustomerService(final AccessTokenRepository accessTokenRepository, final CustomerRepository customerRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer signUp(final CustomerSignUpCommand customerSignUpCommand) {
        final CustomerSignUpRequest customerSignUpRequest = mapToDomain(customerSignUpCommand);
        return customerRepository.save(customerSignUpRequest);
    }

    @Override
    public String signIn(final CustomerSignInCommand customerSignInCommand) {
        final Customer customer = customerRepository.findByEmail(customerSignInCommand.email());
        if (customer.isSamePassword(customerSignInCommand.password())) {
            return accessTokenRepository.create(AuthorizationType.CUSTOMER, customer.id());
        }
        throw new PasswordMissMatchException();
    }

    @Override
    public void signOut(final long userId) {
        accessTokenRepository.delete(AuthorizationType.CUSTOMER, userId);
    }

    private CustomerSignUpRequest mapToDomain(final CustomerSignUpCommand customerSignUpCommand) {
        return new CustomerSignUpRequest(
                customerSignUpCommand.email(),
                customerSignUpCommand.name(),
                customerSignUpCommand.password(),
                customerSignUpCommand.birth(),
                customerSignUpCommand.address(),
                customerSignUpCommand.phone()
        );
    }
}
