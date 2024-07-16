package shopping.customer.domain.repository;

import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerRegistration;

public interface CustomerRepository {
    Customer save(CustomerRegistration customerRegistration);
}