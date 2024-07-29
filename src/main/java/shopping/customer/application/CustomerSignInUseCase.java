package shopping.customer.application;

import shopping.customer.application.command.CustomerSignInCommand;

public interface CustomerSignInUseCase {
    String signIn(CustomerSignInCommand customerSignInCommand);
}