package shopping.member.client.applicaton;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.member.client.applicaton.dto.ClientCreateRequest;
import shopping.member.client.applicaton.dto.ClientLoginRequest;
import shopping.member.client.applicaton.dto.ClientLoginResponse;
import shopping.member.client.domain.Client;
import shopping.member.client.domain.ClientRepository;
import shopping.member.common.application.AuthService;
import shopping.member.common.domain.Password;

@Service
@RequiredArgsConstructor
public class ClientLoginService {

    private final AuthService authService;
    private final ClientRepository clientRepository;

    public void signUp(final ClientCreateRequest request) {
        authService.validateEmail(request.email());

        final Password password = authService.encodePassword(request.password());
        final Client client = new Client(
                request.email(),
                password,
                request.memberName()
        );
        clientRepository.save(client);
    }

    public ClientLoginResponse login(final ClientLoginRequest request) {
        final String accessToken = authService.login(request.email(), request.password());
        return new ClientLoginResponse(accessToken);
    }
}
