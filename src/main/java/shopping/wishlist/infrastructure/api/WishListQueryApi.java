package shopping.wishlist.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.wishlist.infrastructure.api.dto.WishListInfo;
import shopping.wishlist.infrastructure.api.dto.WishListInfoHttpResponse;
import shopping.wishlist.infrastructure.persistence.WishListDao;

import java.util.List;

@RequestMapping("/api/wish-lists")
@RestController
public class WishListQueryApi {

    private final WishListDao wishListDao;

    public WishListQueryApi(final WishListDao wishListDao) {
        this.wishListDao = wishListDao;
    }

    @GetMapping
    public ResponseEntity<WishListInfoHttpResponse> wishList(
            @Authorization({AuthorizationType.CUSTOMER}) final AuthorizationUser authorizationUser,
            @RequestParam(value = "startId", required = false, defaultValue = "1") final long startId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final long limit
    ) {
        final List<WishListInfo> wishListInfos = wishListDao.findByCustomerId(authorizationUser.userId(), startId, limit);
        return ResponseEntity.ok(new WishListInfoHttpResponse(wishListInfos));
    }
}
