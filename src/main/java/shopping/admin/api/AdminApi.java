package shopping.admin.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.admin.api.dto.AdminSignInHttpRequest;
import shopping.admin.api.dto.AdminSignInHttpResponse;
import shopping.admin.api.dto.AdminSignUpHttpRequest;
import shopping.admin.application.AdminSignInUseCase;
import shopping.admin.application.AdminSignUpUseCase;

import java.net.URI;

@RequestMapping("/api/admins")
@RestController
public class AdminApi {
    private final AdminSignUpUseCase adminSignUpUseCase;
    private final AdminSignInUseCase adminSignInUseCase;

    public AdminApi(final AdminSignUpUseCase adminSignUpUseCase, final AdminSignInUseCase adminSignInUseCase) {
        this.adminSignUpUseCase = adminSignUpUseCase;
        this.adminSignInUseCase = adminSignInUseCase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> register(@RequestBody final AdminSignUpHttpRequest adminSignUpHttpRequest) {
        adminSignUpUseCase.signUp(adminSignUpHttpRequest.toCommand());
        return ResponseEntity.created(URI.create("")).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AdminSignInHttpResponse> signIn(@RequestBody final AdminSignInHttpRequest adminSignInHttpRequest) {
        final String accessToken = adminSignInUseCase.signIn(adminSignInHttpRequest.toCommand());
        return ResponseEntity.ok(new AdminSignInHttpResponse(accessToken));
    }
}
