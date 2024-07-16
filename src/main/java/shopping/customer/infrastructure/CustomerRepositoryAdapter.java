package shopping.customer.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerRegistration;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.infrastructure.persistence.CustomerEntity;
import shopping.customer.infrastructure.persistence.CustomerEntityJpaRepository;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerEntityJpaRepository repository;

    public CustomerRepositoryAdapter(final CustomerEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Customer save(final CustomerRegistration customerRegistration) {
        final CustomerEntity customerEntity = repository.save(domainToEntity(customerRegistration));
        return entityToDomain(customerEntity);
    }

    private CustomerEntity domainToEntity(final CustomerRegistration customerRegistration) {
        return new CustomerEntity(
                customerRegistration.email(),
                customerRegistration.name(),
                customerRegistration.address(),
                customerRegistration.birth(),
                customerRegistration.phone(),
                customerRegistration.password());
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
