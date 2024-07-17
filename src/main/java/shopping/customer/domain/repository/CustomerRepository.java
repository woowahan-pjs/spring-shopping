package shopping.customer.domain.repository;

import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;

public interface CustomerRepository {
    Customer save(CustomerSignUpRequest customerSignUpRequest);
    Customer findByEmail(final String email);
}