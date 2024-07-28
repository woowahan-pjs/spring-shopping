package shopping.utils.fake;


import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeAdminRepository implements AdminRepository {
    private final Map<Long, Admin> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Admin save(final Admin AdminSignUpRequest) {
        final boolean exist = storage.values().stream()
                .map(Admin::email)
                .anyMatch(it -> it.equals(AdminSignUpRequest.email()));
        if (exist) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        final var id = idGenerator.incrementAndGet();
        final Admin Admin = new Admin(
                id,
                AdminSignUpRequest.name(),
                AdminSignUpRequest.email(),
                AdminSignUpRequest.password()
        );
        storage.put(id, Admin);
        return Admin;
    }

    @Override
    public Admin findByEmail(final String email) {
        return storage.values().stream()
                .filter(it -> it.email().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}
