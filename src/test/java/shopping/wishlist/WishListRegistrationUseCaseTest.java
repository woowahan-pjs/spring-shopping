package shopping.wishlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.utils.fake.FakeWishListRepository;
import shopping.wishlist.application.WishListRegistrationUseCase;
import shopping.wishlist.application.WishListService;
import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.application.query.WishListRegistrationQuery;
import shopping.wishlist.domain.WishListRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("위시리스트 등록 단위 테스트")
public class WishListRegistrationUseCaseTest {


    private WishListRepository wishListRepository;
    private WishListRegistrationUseCase wishListRegistrationUseCase;

    @BeforeEach
    void setUp() {
        wishListRepository = new FakeWishListRepository();
        wishListRegistrationUseCase = new WishListService(wishListRepository);
    }

    @DisplayName("고객은 위시리스트에 상품을 등록할 수 있다.")
    @Test
    void register() {
        final WishListRegistrationCommand wishListRegistrationCommand = new WishListRegistrationCommand(1L, 1L);
        final WishListRegistrationQuery wishListRegistrationQuery = wishListRegistrationUseCase.register(wishListRegistrationCommand);

        assertAll(
                () -> assertThat(wishListRegistrationQuery).isNotNull(),
                () -> assertThat(wishListRegistrationQuery.id()).isNotNull(),
                () -> assertThat(wishListRegistrationQuery.productId()).isEqualTo(1L),
                () -> assertThat(wishListRegistrationQuery.customerId()).isEqualTo(1L)
        );
    }
}
