package shopping.seller.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.exception.PasswordMissMatchException;
import shopping.seller.application.command.SellerSignInCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.utils.fake.FakeAccessTokenRepository;
import shopping.utils.fake.FakeSellerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static shopping.utils.fixture.SellerFixture.*;

@DisplayName("고객 로그인 테스트")
public class SellerSignInUseCaseTest {

    private AccessTokenRepository accessTokenRepository;
    private SellerRepository sellerRepository;
    private SellerSignInUseCase sellerSignInUseCase;

    @BeforeEach
    void setUp() {
        sellerRepository = new FakeSellerRepository();
        accessTokenRepository = new FakeAccessTokenRepository();
        sellerSignInUseCase = new SellerService(accessTokenRepository, sellerRepository);
    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        sellerRepository.save(new Seller(1L, EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));

        final SellerSignInCommand SellerSignInCommand = new SellerSignInCommand(EMAIL, PASSWORD);
        final String accessToken = sellerSignInUseCase.signIn(SellerSignInCommand);

        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        sellerRepository.save(new Seller(1L, EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        final SellerSignInCommand SellerSignInCommand = new SellerSignInCommand(OTHER_EMAIL, PASSWORD);
        assertThatThrownBy(() -> sellerSignInUseCase.signIn(SellerSignInCommand))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        sellerRepository.save(new Seller(1L, EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        final SellerSignInCommand SellerSignInCommand = new SellerSignInCommand(EMAIL, OTHER_PASSWORD);
        assertThatThrownBy(() -> sellerSignInUseCase.signIn(SellerSignInCommand))
                .isExactlyInstanceOf(PasswordMissMatchException.class);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        assertThatThrownBy(() -> new SellerSignInCommand("test@", PASSWORD))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        assertThatThrownBy(() -> new SellerSignInCommand(EMAIL, "1234"))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }
}
