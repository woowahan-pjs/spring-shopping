package shopping.customer.domain.repository;

import shopping.customer.domain.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);

    Customer findByEmail(final String email);
}