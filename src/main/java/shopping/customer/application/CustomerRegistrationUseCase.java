package shopping.customer.application;

import shopping.customer.domain.Customer;
import shopping.customer.application.command.CustomerCommand;

public interface CustomerRegistrationUseCase {
    Customer register(final CustomerCommand customerCommand);
}
