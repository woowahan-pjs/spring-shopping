package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.common.client.ProductNameValidator;
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;

@Service
@RequiredArgsConstructor
public class ProductRegistrationService {
    private final ProductNameValidator productNameValidator;
    private final ProductCommandService productCommandService;

    public ProductOutput register(ProductRegisterInput input) {
        productNameValidator.validate(input.name());
        return productCommandService.register(input);
    }

    public ProductOutput update(Long id, ProductRegisterInput input) {
        productNameValidator.validate(input.name());
        return productCommandService.update(id, input);
    }
}
