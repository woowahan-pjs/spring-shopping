package shopping.wish.service;

import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.wish.domain.WishList;
import shopping.wish.domain.WishListItem;
import shopping.wish.dto.WishProductResponse;
import shopping.wish.repository.WishListItemRepository;
import shopping.wish.repository.WishListRepository;

import java.util.List;

@Service
public class WishService {

    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;
    private final ProductRepository productRepository;

    public WishService(
            WishListRepository wishListRepository,
            WishListItemRepository wishListItemRepository,
            ProductRepository productRepository
    ) {
        this.wishListRepository = wishListRepository;
        this.wishListItemRepository = wishListItemRepository;
        this.productRepository = productRepository;
    }

    public void addWish(Long memberId, Long productId) {

        WishList wishList = wishListRepository.findByMemberId(memberId)
                .orElseThrow();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        WishListItem item = WishListItem.create(wishList, product);

        wishListItemRepository.save(item);
    }

    public void deleteWish(Long wishId) {

        wishListItemRepository.deleteById(wishId);
    }

    public List<WishProductResponse> getWishList(Long memberId) {

        WishList wishList = wishListRepository.findByMemberId(memberId)
                .orElseThrow();

        return wishListItemRepository.findByWishListId(wishList.getId())
                .stream()
                .map(WishProductResponse::from)
                .toList();
    }
}
