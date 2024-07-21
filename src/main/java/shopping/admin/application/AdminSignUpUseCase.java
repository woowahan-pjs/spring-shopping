package shopping.admin.application;

import shopping.admin.application.command.AdminSignUpCommand;
import shopping.admin.domain.Admin;

public interface AdminSignUpUseCase {
    Admin signUp(AdminSignUpCommand adminSignUpCommand);
}
