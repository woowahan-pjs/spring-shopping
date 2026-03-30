package shopping.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shopping.product.domain.*;
import shopping.product.dto.CreateProductResponse;

public class CreateProductService implements CreateProduct {

    private static final Logger log = LoggerFactory.getLogger(CreateProductService.class);

    private final ProductNameFactory productNameFactory;
    private final SaveProductService saveProductService;

    public CreateProductService(ProductNameFactory productNameFactory,
            SaveProductService saveProductService) {
        this.productNameFactory = productNameFactory;
        this.saveProductService = saveProductService;
    }

    @Override
    public CreateProductResponse execute(String name, long price, String imageUrl) {
        try {
            log.info("[CreateProduct] profanity check start for name='{}'", name);
            ProductName productName = productNameFactory.create(name);
            log.info("[CreateProduct] profanity check done, verified={}", productName.isVerified());
            log.info("[CreateProduct] calling save (transaction begins here)");
            Product product = saveProductService.execute(productName, price, imageUrl);
            log.info("[CreateProduct] save returned (transaction ended)");
            return new CreateProductResponse(product.getId());
        } catch (ProfanityCheckException e) {
            log.warn("[CreateProduct] profanity check failed, saving as unverified", e);
            ProductName productName = productNameFactory.createUnverified(name);
            log.info("[CreateProduct] calling save (transaction begins here)");
            Product product = saveProductService.execute(productName, price, imageUrl);
            log.info("[CreateProduct] save returned (transaction ended)");
            return new CreateProductResponse(product.getId());
        }
    }
}
