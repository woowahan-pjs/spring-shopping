package shopping.seller.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.seller.application.command.SellerSignUpCommand;
import shopping.seller.domain.Seller;
import shopping.seller.domain.SellerRepository;
import shopping.seller.domain.SellerSignUpRequest;
import shopping.utils.fake.FakeSellerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static shopping.utils.fixture.SellerFixture.*;

@DisplayName("판매자 회원가입 테스트")
public class SellerSignUpUseCaseTest {

    private SellerRepository sellerRepository;
    private SellerSignUpUseCase sellerSignUpUseCase;

    @BeforeEach
    void setUp() {
        sellerRepository = new FakeSellerRepository();
        sellerSignUpUseCase = new SellerSignUpService(sellerRepository);
    }

    @DisplayName("미가입 판매자는 이메일과 비밀번호를 입력하면 회원 가입 할 수 있다.")
    @Test
    void signUp() {
        final SellerSignUpCommand sellerSignUpCommand = new SellerSignUpCommand(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final Seller seller = sellerSignUpUseCase.signUp(sellerSignUpCommand);

        assertAll(
                () -> assertNotNull(seller),
                () -> assertThat(seller.email()).isEqualTo(EMAIL),
                () -> assertThat(seller.name()).isEqualTo(NAME),
                () -> assertThat(seller.password()).isEqualTo(PASSWORD),
                () -> assertThat(seller.birth()).isEqualTo(BIRTH),
                () -> assertThat(seller.address()).isEqualTo(ADDRESS),
                () -> assertThat(seller.phone()).isEqualTo(PHONE)
        );
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        assertThatThrownBy(() -> new SellerSignUpCommand("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        assertThatThrownBy(() -> new SellerSignUpCommand(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이미 존재하는 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpDuplicatedEmail() {
        sellerRepository.save(new SellerSignUpRequest(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        assertThatThrownBy(() -> sellerSignUpUseCase.signUp(new SellerSignUpCommand(EMAIL, OTHER_NAME, OTHER_PASSWORD, OTHER_BIRTH, OTHER_ADDRESS, OTHER_PHONE)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
