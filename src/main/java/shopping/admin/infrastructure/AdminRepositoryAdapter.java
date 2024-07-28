package shopping.admin.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
import shopping.admin.infrastructure.persistence.AdminEntity;
import shopping.admin.infrastructure.persistence.AdminEntityJpaRepository;

import java.util.List;

import static shopping.admin.infrastructure.AdminEntityMapper.domainToEntity;
import static shopping.admin.infrastructure.AdminEntityMapper.entityToDomain;

@Component
public class AdminRepositoryAdapter implements AdminRepository {
    private final AdminEntityJpaRepository adminEntityJpaRepository;

    public AdminRepositoryAdapter(final AdminEntityJpaRepository adminEntityJpaRepository) {
        this.adminEntityJpaRepository = adminEntityJpaRepository;
    }

    @Transactional
    @Override
    public Admin save(final Admin admin) {
        final AdminEntity adminEntity = adminEntityJpaRepository.save(domainToEntity(admin));
        return entityToDomain(adminEntity);
    }

    @Override
    public Admin findByEmail(final String email) {
        final List<AdminEntity> all = adminEntityJpaRepository.findAll();
        return adminEntityJpaRepository.findByEmail(email)
                .map(AdminEntityMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}
