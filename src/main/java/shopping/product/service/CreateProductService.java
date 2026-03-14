package shopping.product.service;

import shopping.product.domain.*;

public class CreateProductService implements CreateProduct {

    private final ProductNameFactory productNameFactory;
    private final SaveProductService saveProductService;

    public CreateProductService(ProductNameFactory productNameFactory,
            SaveProductService saveProductService) {
        this.productNameFactory = productNameFactory;
        this.saveProductService = saveProductService;
    }

    @Override
    public Product execute(String name, long price, String imageUrl) {
        try {
            ProductName productName = productNameFactory.create(name);
            return saveProductService.execute(productName, price, imageUrl);
        } catch (ProfanityCheckException e) {
            ProductName productName = productNameFactory.createUnverified(name);
            return saveProductService.execute(productName, price, imageUrl);
        }
    }
}
