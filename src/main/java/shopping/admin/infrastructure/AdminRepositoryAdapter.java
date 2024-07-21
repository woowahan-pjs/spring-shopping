package shopping.admin.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
import shopping.admin.domain.AdminSignUpRequest;
import shopping.admin.infrastructure.persistence.AdminEntity;
import shopping.admin.infrastructure.persistence.AdminEntityJpaRepository;

@Component
public class AdminRepositoryAdapter implements AdminRepository {
    private final AdminEntityJpaRepository adminEntityJpaRepository;

    public AdminRepositoryAdapter(final AdminEntityJpaRepository adminEntityJpaRepository) {
        this.adminEntityJpaRepository = adminEntityJpaRepository;
    }

    @Transactional
    @Override
    public Admin save(final AdminSignUpRequest adminSignUpRequest) {
        final AdminEntity adminEntity = adminEntityJpaRepository.save(domainToEntity(adminSignUpRequest));
        return entityToDomain(adminEntity);
    }

    @Override
    public Admin findByEmail(final String email) {
        return adminEntityJpaRepository.findByEmail(email)
                .map(this::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    private Admin entityToDomain(final AdminEntity adminEntity) {
        return new Admin(adminEntity.getId(), adminEntity.getName(), adminEntity.getEmail(), adminEntity.getPassword());
    }

    private AdminEntity domainToEntity(final AdminSignUpRequest adminSignUpRequest) {
        return new AdminEntity(adminSignUpRequest.name(), adminSignUpRequest.email(), adminSignUpRequest.password());
    }
}
