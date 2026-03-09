package shopping.wish.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.WishList;
import shopping.wish.domain.WishListItem;
import shopping.wish.repository.WishListItemRepository;
import shopping.wish.repository.WishListRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WishServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private WishListItemRepository wishListItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @Test
    void 위시리스트에_상품을_추가한다() {

        //given
        Long memberId = 1L;
        Long productId = 1L;

        WishList wishList = mock(WishList.class);
        Product product = mock(Product.class);

        given(wishListRepository.findByMemberId(memberId))
                .willReturn(Optional.of(wishList));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        //when
        wishService.addWish(memberId, productId);

        //then
        verify(wishListItemRepository).save(any(WishListItem.class));
    }
}
