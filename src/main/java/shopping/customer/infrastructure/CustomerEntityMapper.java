package shopping.customer.infrastructure;

import shopping.customer.domain.Customer;
import shopping.customer.infrastructure.persistence.CustomerEntity;

public class CustomerEntityMapper {
    private CustomerEntityMapper() {
    }

    public static CustomerEntity init(final Customer customer) {
        return new CustomerEntity(
                null,
                customer.email(),
                customer.name(),
                customer.address(),
                customer.birth(),
                customer.phone(),
                customer.password());
    }

    public static Customer entityToDomain(final CustomerEntity customerEntity) {
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
