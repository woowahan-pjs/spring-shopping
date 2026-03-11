package shopping.product.api.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shopping.product.api.command.dto.ProductRegisterRequest;
import shopping.product.api.query.dto.ProductDetailResponse;
import shopping.product.service.ProductCommandService;
import shopping.product.service.dto.ProductRegisterInput;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductCommandController {
    private final ProductCommandService productCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailResponse register(@RequestBody ProductRegisterRequest request) {
        return ProductDetailResponse.from(productCommandService.register(new ProductRegisterInput(request.name(), request.price(), request.imageUrl())));
    }

    @PutMapping("/{productId}")
    public ProductDetailResponse update(@PathVariable Long productId, @RequestBody ProductRegisterRequest request) {
        return ProductDetailResponse.from(productCommandService.update(productId, new ProductRegisterInput(request.name(), request.price(), request.imageUrl())));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long productId) {
        productCommandService.delete(productId);
    }
}
