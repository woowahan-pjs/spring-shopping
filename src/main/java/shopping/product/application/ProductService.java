package shopping.product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.constant.enums.YesNo;
import shopping.exception.BadRequestException;
import shopping.exception.NotFoundException;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.Products;
import shopping.product.dto.ProductRequest;
import shopping.product.dto.ProductResponse;
import shopping.slang.application.SlangService;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class ProductService {
    private SlangService slangService;
    private ProductRepository productRepository;


    public ProductService(ProductRepository productRepository, SlangService slangService) {
        this.productRepository = productRepository;
        this.slangService = slangService;
    }


    @Transactional
    public ProductResponse.ProductDetail createProduct(ProductRequest.RegProduct request) {
        checkPrdctNmContainSlang(request.getPrdctNm());
        Product persistProduct = productRepository.save(request.toProduct());
        persistProduct.updateProductId();

        return ProductResponse.ProductDetail.from(persistProduct);
    }

    private void checkPrdctNmContainSlang(String prdctNm) {
        if(slangService.hasSlang(prdctNm)) {
            throw new BadRequestException("비속어가 포함되어 있습니다.");
        }
    }


    public ProductResponse.ProductsRes findAllProducts() {
        Products products = findAllToProducts();

        return ProductResponse.ProductsRes.from(products);
    }




    public ProductResponse.ProductDetail findProductDetailResponseBySn(Long id) {
        Product persistProduct = findProductByPrdctSn(id);
        return ProductResponse.ProductDetail.from(persistProduct);
    }

    @Transactional
    public ProductResponse.ProductDetail updateProductById(Long id, ProductRequest.ModProduct request) {
        Product persistProduct = findProductByPrdctSn(id);
        persistProduct.updateNameOrImage(request.getPrdctNm(), request.getImage(), request.getPrice());
        return ProductResponse.ProductDetail.from(persistProduct);
    }

    @Transactional
    public void deleteProductById(Long id) {
        Product persistProduct = findProductByPrdctSn(id);
        persistProduct.updateDelYn(YesNo.Y);
    }


    public Product findProductByPrdctSn(Long sn) {
        return productRepository.findById(sn)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));
    }

    private Products findAllToProducts() {
        List<Product> products = productRepository.findAll();
        return new Products(products);
    }


}
