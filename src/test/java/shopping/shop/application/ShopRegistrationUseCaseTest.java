package shopping.shop.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.shop.application.command.ShopRegistrationCommand;
import shopping.shop.domain.ShopRepository;
import shopping.utils.fake.FakeShopRepository;
import shopping.utils.fixture.ShopFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상점 등록 단위 테스트")
public class ShopRegistrationUseCaseTest {

    private ShopRepository shopRepository;
    private ShopRegistrationUseCase shopRegistrationUseCase;

    @BeforeEach
    void setUp() {
        shopRepository = new FakeShopRepository();
        shopRegistrationUseCase = new ShopRegistrationService(shopRepository);
    }

    @DisplayName("판매자는 상점을 등록할 수 있다.")
    @Test
    void register() {
        // given
        final var shopRegistrationCommand = new ShopRegistrationCommand(ShopFixture.SELLER_ID, ShopFixture.NAME);

        // when
        final var shopRegistrationQuery = shopRegistrationUseCase.register(shopRegistrationCommand);

        // then
        assertAll(
                () -> assertThat(shopRegistrationQuery).isNotNull(),
                () -> assertThat(shopRegistrationQuery.id()).isNotNull(),
                () -> assertThat(shopRegistrationQuery.sellerId()).isEqualTo(ShopFixture.SELLER_ID),
                () -> assertThat(shopRegistrationQuery.name()).isEqualTo(ShopFixture.NAME)
        );
    }
}
