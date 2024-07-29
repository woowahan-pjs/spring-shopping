package shopping.customer.application;

import shopping.customer.application.command.CustomerSignUpCommand;
import shopping.customer.domain.Customer;

public interface CustomerSignUpUseCase {
    Customer signUp(final CustomerSignUpCommand customerSignUpCommand);
}
