package shopping.customer.application;

import shopping.customer.application.command.CustomerSignInCommand;
import shopping.customer.domain.AccessToken;

interface CustomerSignInUseCase {
    AccessToken signIn(CustomerSignInCommand customerSignInCommand);
}