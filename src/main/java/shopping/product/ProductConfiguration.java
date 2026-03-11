package shopping.product;

import shopping.product.domain.*;
import shopping.product.service.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

    @Bean
    public ProductNameFactory productNameFactory(ProfanityChecker profanityChecker) {
        return new ProductNameFactory(profanityChecker);
    }

    @Bean
    public SaveProductService saveProductService(ProductRepository productRepository) {
        return new SaveProductService(productRepository);
    }

    @Bean
    public ModifyProductService modifyProductService(ProductRepository productRepository) {
        return new ModifyProductService(productRepository);
    }

    @Bean
    public CreateProduct createProduct(ProductNameFactory productNameFactory,
            SaveProductService saveProductService) {
        return new CreateProductService(productNameFactory, saveProductService);
    }

    @Bean
    public FindProduct findProduct(ProductRepository productRepository) {
        return new FindProductService(productRepository);
    }

    @Bean
    public UpdateProduct updateProduct(ProductNameFactory productNameFactory,
            ModifyProductService modifyProductService) {
        return new UpdateProductService(productNameFactory, modifyProductService);
    }

    @Bean
    public DeleteProduct deleteProduct(ProductRepository productRepository,
            ApplicationEventPublisher eventPublisher) {
        return new DeleteProductService(productRepository, eventPublisher);
    }
}
