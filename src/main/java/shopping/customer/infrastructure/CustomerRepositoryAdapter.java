package shopping.customer.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.customer.domain.Customer;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.infrastructure.persistence.CustomerEntity;
import shopping.customer.infrastructure.persistence.CustomerEntityJpaRepository;

import static shopping.customer.infrastructure.CustomerEntityMapper.domainToEntity;
import static shopping.customer.infrastructure.CustomerEntityMapper.entityToDomain;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerEntityJpaRepository repository;

    public CustomerRepositoryAdapter(final CustomerEntityJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Customer save(final Customer customer) {
        final CustomerEntity customerEntity = repository.save(domainToEntity(customer));
        return entityToDomain(customerEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByEmail(final String email) {
        return repository.findByEmail(email)
                .map(CustomerEntityMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
