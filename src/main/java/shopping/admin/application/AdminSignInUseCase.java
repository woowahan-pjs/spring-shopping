package shopping.admin.application;

import shopping.admin.application.command.AdminSignInCommand;

public interface AdminSignInUseCase {
    String signIn(final AdminSignInCommand adminSignInCommand);
}
