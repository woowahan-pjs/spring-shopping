package shopping.admin.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
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
    public Admin save(final Admin admin) {
        final AdminEntity adminEntity = adminEntityJpaRepository.save(AdminEntityMapper.init(admin));
        return AdminEntityMapper.entityToDomain(adminEntity);
    }

    @Override
    public Admin findByEmail(final String email) {
        return adminEntityJpaRepository.findByEmail(email)
                .map(AdminEntityMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
