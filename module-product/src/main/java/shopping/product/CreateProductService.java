package shopping.product;

public class CreateProductService implements CreateProduct {

    private final ProductRepository productRepository;
    private final ProductNameFactory productNameFactory;

    public CreateProductService(ProductRepository productRepository,
            ProductNameFactory productNameFactory) {
        this.productRepository = productRepository;
        this.productNameFactory = productNameFactory;
    }

    @Override
    public Product execute(String name, long price, String imageUrl) {
        ProductName productName = productNameFactory.create(name);
        Product product = new Product(productName, price, imageUrl);
        return productRepository.save(product);
    }
}
