package shopping.member.client.applicaton;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakeClientRepository;
import shopping.fake.FakeMemberRepository;
import shopping.fake.FakePasswordEncoder;
import shopping.fake.InMemoryMembers;
import shopping.member.client.applicaton.dto.ClientCreateRequest;
import shopping.member.client.domain.ClientRepository;
import shopping.member.common.application.AuthService;
import shopping.member.common.domain.MemberRepository;
import shopping.member.common.exception.InvalidEmailException;

@DisplayName("ClientLoginService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClientLoginServiceTest {

    private ClientLoginService clientService;

    @BeforeEach
    void setUp() {
        final InMemoryMembers inMemoryMembers = new InMemoryMembers();
        final MemberRepository memberRepository = new FakeMemberRepository(inMemoryMembers);
        final AuthService authService = new AuthService(
                memberRepository,
                new FakePasswordEncoder(),
                (email, token) -> email + " " + token);
        final ClientRepository clientRepository = new FakeClientRepository(inMemoryMembers);
        clientService = new ClientLoginService(authService, clientRepository);
    }

    @Test
    void 회원가입을_진행할_수_있다() {
        assertThatNoException().isThrownBy(() -> clientService.signUp(createRequest()));
    }

    @Test
    void 같은_이메일로_중복해서_가입할_수_없다() {
        final ClientCreateRequest request = createRequest();
        clientService.signUp(request);

        assertThatThrownBy(() -> clientService.signUp(request))
                .isInstanceOf(InvalidEmailException.class);
    }

    private ClientCreateRequest createRequest() {
        return new ClientCreateRequest(
                "test@test.com",
                "1234",
                "test"
        );
    }
}