package shopping.utils.fake;

import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCustomerRepository implements CustomerRepository {
    private final Map<Long, Customer> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Customer save(final CustomerSignUpRequest customerSignUpRequest) {
        final boolean exist = storage.values().stream()
                .map(Customer::getEmail)
                .anyMatch(it -> it.equals(customerSignUpRequest.email()));
        if (exist) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        final var id = idGenerator.incrementAndGet();
        final Customer customer = new Customer(
                id,
                customerSignUpRequest.email(),
                customerSignUpRequest.name(),
                customerSignUpRequest.password(),
                customerSignUpRequest.birth(),
                customerSignUpRequest.address(),
                customerSignUpRequest.phone()
        );
        storage.put(id, customer);
        return customer;
    }

    @Override
    public Customer findByEmail(final String email) {
        return storage.values().stream()
                .filter(it -> it.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}