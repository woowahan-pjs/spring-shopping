package shopping.wish.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shopping.common.exception.ProductNotFoundException;
import shopping.common.exception.WishListNotFoundException;
import shopping.member.domain.Member;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.WishList;
import shopping.wish.domain.WishListItem;
import shopping.wish.dto.WishProductResponse;
import shopping.wish.repository.WishListItemRepository;
import shopping.wish.repository.WishListRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void 위시리스트_아이템을_삭제한다() {

        Long wishId = 1L;

        wishService.deleteWish(wishId);

        verify(wishListItemRepository).deleteById(wishId);
    }

    @Test
    void 위시리스트_아이템_목록을_조회한다() {

        //given
        Long memberId = 1L;
        Member member = Member.create("test@test.com", "123456");

        WishList wishList = WishList.create(member);

        ReflectionTestUtils.setField(wishList, "id", 1L);

        Product product = new Product("아이폰", 1, 1_000_000, "image.jpg");
        ReflectionTestUtils.setField(product, "id", 1L);

        WishListItem item = WishListItem.create(wishList, product);

        given(wishListRepository.findByMemberId(memberId))
                .willReturn(Optional.of(wishList));

        given(wishListItemRepository.findByWishListId(1L))
                .willReturn(List.of(item));

        //when
        List<WishProductResponse> result = wishService.getWishList(memberId);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getProductId()).isEqualTo(1L);
    }

    @Test
    void 위시리스트가_없으면_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when & then
        assertThrows(WishListNotFoundException.class, () -> wishService.getWishList(memberId));
    }

    @Test
    void 존재하지_않는_상품이면_예외가_발생한다() {
        // given
        Long productId = 999L;

        // when & then
        assertThrows(ProductNotFoundException.class, () -> wishService.addWish(1L, productId));
    }
}
