package shopping.member.owner.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import shopping.fake.FakePasswordEncoder;
import shopping.fake.FakeMemberRepository;
import shopping.fake.InMemoryMembers;
import shopping.fake.FakeOwnerRepository;
import shopping.member.common.application.AuthService;
import shopping.member.common.domain.MemberRepository;
import shopping.member.common.exception.InvalidEmailException;
import shopping.member.owner.application.dto.OwnerCreateRequest;
import shopping.member.owner.domain.OwnerRepository;

@DisplayName("OwnerLoginService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OwnerLoginServiceTest {

    private OwnerLoginService ownerService;

    @BeforeEach
    void setUp() {
        final InMemoryMembers inMemoryMembers = new InMemoryMembers();
        final MemberRepository memberRepository = new FakeMemberRepository(inMemoryMembers);
        final AuthService authService = new AuthService(memberRepository,
                new FakePasswordEncoder());
        final OwnerRepository ownerRepository = new FakeOwnerRepository(inMemoryMembers);
        ownerService = new OwnerLoginService(authService, ownerRepository);
    }

    @Test
    void 회원가입을_진행할_수_있다() {
        assertThatNoException().isThrownBy(() -> ownerService.signUp(createRequest()));
    }

    @Test
    void 같은_이메일로_중복해서_가입할_수_없다() {
        final OwnerCreateRequest request = createRequest();
        ownerService.signUp(request);

        assertThatThrownBy(() -> ownerService.signUp(request))
                .isInstanceOf(InvalidEmailException.class);
    }

    private OwnerCreateRequest createRequest() {
        return new OwnerCreateRequest(
                "admin@test.com",
                "1234",
                "admin"
        );
    }
}