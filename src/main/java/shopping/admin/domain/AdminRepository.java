package shopping.admin.domain;

public interface AdminRepository {
    Admin save(final AdminSignUpRequest adminSignUpRequest);

    Admin findByEmail(String email);
}
