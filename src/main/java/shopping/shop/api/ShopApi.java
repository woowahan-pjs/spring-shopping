package shopping.shop.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.shop.api.dto.ShopRegistrationRequest;
import shopping.shop.api.dto.ShopRegistrationResponse;
import shopping.shop.application.ShopRegistrationUseCase;
import shopping.shop.domain.Shop;

import java.net.URI;

@RequestMapping("/api/sellers/shops")
@RestController
public class ShopApi {

    private final ShopRegistrationUseCase shopRegistrationUseCase;

    public ShopApi(final ShopRegistrationUseCase shopRegistrationUseCase) {
        this.shopRegistrationUseCase = shopRegistrationUseCase;
    }

    @PostMapping
    public ResponseEntity<ShopRegistrationResponse> registration(
            @RequestBody final ShopRegistrationRequest request,
            @Authorization({AuthorizationType.SELLER}) final AuthorizationUser authorizationUser
    ) {
        final Shop shop = shopRegistrationUseCase.register(request.toCommand(authorizationUser.getUserId()));
        return ResponseEntity.created(URI.create("/api/sellers/shops/" + shop.id()))
                .body(new ShopRegistrationResponse(shop.id()));
    }
}
