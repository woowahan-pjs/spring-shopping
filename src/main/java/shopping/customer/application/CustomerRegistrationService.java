package shopping.customer.application;

import org.springframework.stereotype.Service;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerRegistration;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.application.command.CustomerCommand;

@Service
public class CustomerRegistrationService implements CustomerRegistrationUseCase {

    private final CustomerRepository customerRepository;

    public CustomerRegistrationService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer register(final CustomerCommand customerCommand) {
        final CustomerRegistration customerRegistration = mapToDomain(customerCommand);
        return customerRepository.save(customerRegistration);
    }

    private CustomerRegistration mapToDomain(final CustomerCommand customerCommand) {
        return new CustomerRegistration(
                customerCommand.email(),
                customerCommand.name(),
                customerCommand.password(),
                customerCommand.birth(),
                customerCommand.address(),
                customerCommand.phone()
        );
    }
}
