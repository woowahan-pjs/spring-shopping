package shopping.customer.application;

import org.springframework.stereotype.Service;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.application.command.CustomerSignUpCommand;

@Service
public class CustomerSignUpService implements CustomerSignUpUseCase {

    private final CustomerRepository customerRepository;

    public CustomerSignUpService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer signUp(final CustomerSignUpCommand customerSignUpCommand) {
        final CustomerSignUpRequest customerSignUpRequest = mapToDomain(customerSignUpCommand);
        return customerRepository.save(customerSignUpRequest);
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
