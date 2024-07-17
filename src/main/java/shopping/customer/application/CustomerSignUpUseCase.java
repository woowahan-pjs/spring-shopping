package shopping.customer.application;

import shopping.customer.domain.Customer;
import shopping.customer.application.command.CustomerSignUpCommand;

public interface CustomerSignUpUseCase {
    Customer signUp(final CustomerSignUpCommand customerSignUpCommand);
}
