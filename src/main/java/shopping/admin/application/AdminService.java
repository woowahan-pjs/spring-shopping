package shopping.admin.application;

import org.springframework.stereotype.Service;
import shopping.admin.application.command.AdminSignInCommand;
import shopping.admin.application.command.AdminSignUpCommand;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
import shopping.admin.domain.AdminSignUpRequest;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.auth.AuthorizationType;
import shopping.common.exception.PasswordMissMatchException;

@Service
public class AdminService implements AdminSignUpUseCase, AdminSignInUseCase {
    private final AccessTokenRepository accessTokenRepository;
    private final AdminRepository adminRepository;

    public AdminService(final AccessTokenRepository accessTokenRepository, final AdminRepository adminRepository) {
        this.accessTokenRepository = accessTokenRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin signUp(final AdminSignUpCommand adminSignUpCommand) {
        final AdminSignUpRequest adminSignUpRequest = mapToDomain(adminSignUpCommand);
        return adminRepository.save(adminSignUpRequest);
    }

    @Override
    public String signIn(final AdminSignInCommand adminSignInCommand) {
        final Admin admin = adminRepository.findByEmail(adminSignInCommand.email());
        if (admin.isSamePassword(adminSignInCommand.password())) {
            return accessTokenRepository.create(AuthorizationType.ADMIN, admin.id());
        }
        throw new PasswordMissMatchException();
    }

    private AdminSignUpRequest mapToDomain(final AdminSignUpCommand adminSignUpCommand) {
        return new AdminSignUpRequest(
                adminSignUpCommand.email(),
                adminSignUpCommand.name(),
                adminSignUpCommand.password()
        );
    }
}
