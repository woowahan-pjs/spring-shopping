package shopping.wishlist.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.wishlist.application.WishListRegistrationUseCase;
import shopping.wishlist.application.query.WishListRegistrationQuery;
import shopping.wishlist.infrastructure.api.dto.WishListRegistrationRequest;
import shopping.wishlist.infrastructure.api.dto.WishListRegistrationResponse;

import java.net.URI;

@RequestMapping("/api/wish-lists")
@RestController
public class WishListApi {

    private final WishListRegistrationUseCase wishListRegistrationUseCase;

    public WishListApi(final WishListRegistrationUseCase wishListRegistrationUseCase) {
        this.wishListRegistrationUseCase = wishListRegistrationUseCase;
    }

    @PostMapping
    public ResponseEntity<WishListRegistrationResponse> register(
            @RequestBody final WishListRegistrationRequest request,
            @Authorization({AuthorizationType.CUSTOMER}) final AuthorizationUser authorizationUser
    ) {
        final WishListRegistrationQuery wishListRegistrationQuery = wishListRegistrationUseCase.register(request.toCommand(authorizationUser.userId()));
        return ResponseEntity.created(URI.create("/api/wish-lists/" + wishListRegistrationQuery.id()))
                .body(new WishListRegistrationResponse(wishListRegistrationQuery.id()));
    }
}
