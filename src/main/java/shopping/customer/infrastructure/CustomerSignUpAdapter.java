package shopping.customer.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.infrastructure.persistence.CustomerEntity;
import shopping.customer.infrastructure.persistence.CustomerEntityJpaRepository;

@Component
public class CustomerSignUpAdapter implements CustomerRepository {

    private final CustomerEntityJpaRepository repository;

    public CustomerSignUpAdapter(final CustomerEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Customer save(final CustomerSignUpRequest customerSignUpRequest) {
        final CustomerEntity customerEntity = repository.save(domainToEntity(customerSignUpRequest));
        return entityToDomain(customerEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByEmail(final String email) {
        final CustomerEntity customerEntity = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException());
        return entityToDomain(customerEntity);
    }

    private CustomerEntity domainToEntity(final CustomerSignUpRequest customerSignUpRequest) {
        return new CustomerEntity(
                customerSignUpRequest.email(),
                customerSignUpRequest.name(),
                customerSignUpRequest.address(),
                customerSignUpRequest.birth(),
                customerSignUpRequest.phone(),
                customerSignUpRequest.password());
    }

    private Customer entityToDomain(final CustomerEntity customerEntity) {
        return new Customer(
                customerEntity.getId(),
                customerEntity.getEmail(),
                customerEntity.getName(),
                customerEntity.getPassword(),
                customerEntity.getBirth(),
                customerEntity.getAddress(),
                customerEntity.getPhone()
        );
    }
}
