package shopping.admin.domain;

public interface AdminRepository {
    Admin save(final Admin admin);

    Admin findByEmail(String email);
}
