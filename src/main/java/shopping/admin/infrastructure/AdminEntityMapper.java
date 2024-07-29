package shopping.admin.infrastructure;

import shopping.admin.domain.Admin;
import shopping.admin.infrastructure.persistence.AdminEntity;

public class AdminEntityMapper {
    private AdminEntityMapper() {
    }

    public static AdminEntity domainToEntity(final Admin admin) {
        return new AdminEntity(admin.id(), admin.name(), admin.email(), admin.password());
    }

    public static Admin entityToDomain(final AdminEntity adminEntity) {
        return new Admin(adminEntity.getId(), adminEntity.getName(), adminEntity.getEmail(), adminEntity.getPassword());
    }
}
