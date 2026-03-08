package shopping.product;

import java.util.NoSuchElementException;

public class UpdateProductService implements UpdateProduct {

    private final ProductRepository productRepository;
    private final ProductNameFactory productNameFactory;

    public UpdateProductService(ProductRepository productRepository,
            ProductNameFactory productNameFactory) {
        this.productRepository = productRepository;
        this.productNameFactory = productNameFactory;
    }

    @Override
    public Product execute(Long id, String name, long price, String imageUrl) {
        ProductName productName = productNameFactory.create(name);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        product.update(productName, price, imageUrl);
        return productRepository.save(product);
    }
}
