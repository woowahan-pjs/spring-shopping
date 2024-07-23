package shopping.shop.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.shop.application.ShopRegistrationUseCase;
import shopping.shop.application.query.ShopRegistrationQuery;
import shopping.shop.infrastructure.api.dto.ShopRegistrationRequest;
import shopping.shop.infrastructure.api.dto.ShopRegistrationResponse;

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
        final ShopRegistrationQuery shopRegistrationQuery = shopRegistrationUseCase.register(request.toCommand(authorizationUser.userId()));
        return ResponseEntity.created(URI.create("/api/sellers/shops/" + shopRegistrationQuery.id()))
                .body(new ShopRegistrationResponse(shopRegistrationQuery.id()));
    }
}
