package shopping.product.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.product.infrastructure.api.dto.ProductRegistrationHttpRequest;
import shopping.product.infrastructure.api.dto.ProductRegistrationHttpResponse;
import shopping.product.application.ProductRegistrationUseCase;
import shopping.product.domain.Product;

import java.net.URI;

@RequestMapping("/internal-api/shops")
@RestController
public class ProductApi {

    private final ProductRegistrationUseCase productRegistrationUseCase;

    public ProductApi(final ProductRegistrationUseCase productRegistrationUseCase) {
        this.productRegistrationUseCase = productRegistrationUseCase;
    }

    @PostMapping("/{shopId}/products")
    public ResponseEntity<?> registerProduct(
            @PathVariable(name = "shopId") final long shopId,
            @Authorization({AuthorizationType.SELLER}) AuthorizationUser authorizationUser,
            @RequestBody final ProductRegistrationHttpRequest request
    ) {
        final Product product = productRegistrationUseCase.register(request.toCommand(shopId, authorizationUser.userId()));
        return ResponseEntity.created(URI.create("/internal-api/shops/" + shopId + "/products/" + product.id()))
                .body(new ProductRegistrationHttpResponse(product.id()));
    }
}
