package shopping.member.owner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.member.common.application.AuthService;
import shopping.member.common.domain.Password;
import shopping.member.owner.application.dto.OwnerCreateRequest;
import shopping.member.owner.application.dto.OwnerLoginRequest;
import shopping.member.owner.domain.Owner;
import shopping.member.owner.domain.OwnerRepository;

@Service
@RequiredArgsConstructor
public class OwnerLoginService {

    private final AuthService authService;
    private final OwnerRepository ownerRepository;

    public void signUp(final OwnerCreateRequest request) {
        authService.validateEmail(request.email());

        final Password password = authService.encodePassword(request.password());
        final Owner owner = new Owner(
                request.email(),
                password,
                request.memberName()
        );
        ownerRepository.save(owner);
    }

    public String login(final OwnerLoginRequest request) {
        return authService.login(request.email(), request.password(), "Owner");
    }
}
