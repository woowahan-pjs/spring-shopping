package shopping.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.exception.NotFoundException;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;


@Service
@Transactional(readOnly = true)
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional
    public ProductResponse.ProductDetail createProduct(ProductRequest.RegProduct request) {
        Product persistProduct = productRepository.save(request.toProduct());
        persistProduct.updateProductId();

        return ProductResponse.ProductDetail.from(persistProduct);
    }


    private Product findProductByPrdctSn(Long sn) {
        return productRepository.findById(sn)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));
    }



}
