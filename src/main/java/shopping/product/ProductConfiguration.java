package shopping.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

    @Bean
    public CreateProduct createProduct(ProductRepository productRepository,
            ProfanityChecker profanityChecker) {
        return new CreateProductService(productRepository, profanityChecker);
    }

    @Bean
    public FindProduct findProduct(ProductRepository productRepository) {
        return new FindProductService(productRepository);
    }

    @Bean
    public UpdateProduct updateProduct(ProductRepository productRepository,
            ProfanityChecker profanityChecker) {
        return new UpdateProductService(productRepository, profanityChecker);
    }

    @Bean
    public DeleteProduct deleteProduct(ProductRepository productRepository) {
        return new DeleteProductService(productRepository);
    }
}
