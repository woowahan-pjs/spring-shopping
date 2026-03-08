package shopping.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

    @Bean
    public ProductNameFactory productNameFactory(ProfanityChecker profanityChecker) {
        return new ProductNameFactory(profanityChecker);
    }

    @Bean
    public CreateProduct createProduct(ProductRepository productRepository,
            ProductNameFactory productNameFactory) {
        return new CreateProductService(productRepository, productNameFactory);
    }

    @Bean
    public FindProduct findProduct(ProductRepository productRepository) {
        return new FindProductService(productRepository);
    }

    @Bean
    public UpdateProduct updateProduct(ProductRepository productRepository,
            ProductNameFactory productNameFactory) {
        return new UpdateProductService(productRepository, productNameFactory);
    }

    @Bean
    public DeleteProduct deleteProduct(ProductRepository productRepository) {
        return new DeleteProductService(productRepository);
    }
}
