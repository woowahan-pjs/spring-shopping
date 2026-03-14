package shopping.product.service;

import shopping.product.domain.*;

import java.util.UUID;

public class UpdateProductService implements UpdateProduct {

    private final ProductNameFactory productNameFactory;
    private final ModifyProductService modifyProductService;

    public UpdateProductService(ProductNameFactory productNameFactory,
            ModifyProductService modifyProductService) {
        this.productNameFactory = productNameFactory;
        this.modifyProductService = modifyProductService;
    }

    @Override
    public Product execute(UUID id, String name, long price, String imageUrl) {
        ProductName productName = productNameFactory.create(name);
        return modifyProductService.execute(id, productName, price, imageUrl);
    }
}
