package shopping.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.constant.enums.YesNo;
import shopping.exception.NotFoundException;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;

import java.util.List;


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


    public ProductResponse.Products findAllProducts() {
        List<Product> products = productRepository.findAll();

        return ProductResponse.Products.from(products);
    }


    public ProductResponse.ProductDetail findProductDetailResponseBySn(Long id) {
        Product persistProduct = findProductByPrdctSn(id);
        return ProductResponse.ProductDetail.from(persistProduct);
    }

    @Transactional
    public ProductResponse.ProductDetail updateProductById(Long id, ProductRequest.ModProduct request) {
        Product persistProduct = findProductByPrdctSn(id);
        persistProduct.updateNameOrImage(request.getPrdctNm(), request.getImage());
        return ProductResponse.ProductDetail.from(persistProduct);
    }

    @Transactional
    public void deleteProductById(Long id) {
        Product persistProduct = findProductByPrdctSn(id);
        persistProduct.updateDelYn(YesNo.Y);
    }


    private Product findProductByPrdctSn(Long sn) {
        return productRepository.findById(sn)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));
    }



}
