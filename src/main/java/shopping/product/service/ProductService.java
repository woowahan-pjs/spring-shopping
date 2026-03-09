package shopping.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shopping.infra.client.purgomalum.PurgoMalumAdapter;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.NotFoundProductException;
import shopping.product.domain.Product;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductSaveRequest;
import shopping.product.dto.ProductSearchRequest;
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.dto.ProductsSearchResponse;
import shopping.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final PurgoMalumAdapter purgoMalumAdapter;

    /**
     * 주어진 상품 ID를 이용하여 활성화된 상품 정보를 조회하고, ProductResponse로 반환합니다.
     *
     * @param productId 조회할 상품의 고유 ID입니다.
     * @return 조회된 상품 정보를 담고 있는 ProductResponse 객체를 반환합니다. 활성화된 상품이 존재하지 않을 경우 예외가 발생합니다.
     */
    @Transactional(readOnly = true)
    public ProductResponse getProduct(final Long productId) {
        final Product product =
                productRepository
                        .findProductByIdAndIsUse(productId, true)
                        .orElseThrow(NotFoundProductException::new);

        return ProductResponse.from(
                product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    /**
     * 상품 검색 조건과 페이징 정보를 사용하여 등록된 상품 리스트를 조회하고, ProductsSearchResponse 객체로 반환합니다.
     *
     * @param request 상품 검색 조건을 담고 있는 ProductSearchRequest 객체입니다. 검색 조건으로는 상품 이름, 최소 가격, 최대 가격이
     *     포함됩니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체입니다.
     * @return 검색된 상품 리스트를 담고 있는 ProductsSearchResponse 객체를 반환합니다.
     */
    @Transactional(readOnly = true)
    public ProductsSearchResponse searchProduct(
            final ProductSearchRequest request, final Pageable pageable) {
        final Page<Product> products =
                productRepository.search(
                        request.name(), request.fromPrice(), request.toPrice(), pageable);

        return ProductsSearchResponse.from(products.toList(), products.getPageable());
    }

    /**
     * 상품 정보를 저장하고 생성된 상품의 고유 ID를 반환합니다. 요청된 상품 이름에 비속어가 포함된 경우 예외를 발생시킵니다.
     *
     * @param request 저장할 상품 정보를 포함한 ProductSaveRequest 객체입니다.
     * @return 저장된 상품의 고유 ID를 반환합니다.
     */
    @Transactional
    public Long registerProduct(final Long userId, final ProductSaveRequest request) {
        validProductNameProfanity(request.name());

        final Product product = productRepository.save(Product.of(userId, request));

        return product.getId();
    }

    /**
     * 상품 정보를 수정합니다. 주어진 사용자 ID와 상품 ID에 해당하는 활성화된 상품을 조회하고, 요청 정보를 기반으로 수정합니다. 상품 이름이 변경될 경우 비속어 검사를
     * 진행합니다.
     *
     * @param userId 수정하려는 상품을 소유한 사용자의 고유 ID입니다.
     * @param productId 수정하려는 상품의 고유 ID입니다.
     * @param request 수정할 상품 정보를 포함한 ProductUpdateRequest 객체입니다.
     */
    @Transactional
    public void modifyProduct(
            final Long userId, final Long productId, final ProductUpdateRequest request) {
        // 1. 상품이 존재하는지 확인
        final Product product =
                productRepository
                        .findProductByIdAndUserIdAndIsUse(productId, userId, true)
                        .orElseThrow(NotFoundProductException::new);

        // 2. 상품 명이 변경되었다면, 비속어가 존재하는지 확인
        if (!product.isEqualsNameTo(request.name())) {
            validProductNameProfanity(request.name());
        }

        product.modify(request);
    }

    /**
     * 주어진 사용자 ID와 상품 ID를 기반으로 활성화된 상품을 조회하고 상품을 비활성화 처리합니다. 상품이 존재하지 않을 경우 NotFoundProductException
     * 예외를 발생시킵니다.
     *
     * @param userId 상품을 소유한 사용자의 고유 ID입니다.
     * @param productId 비활성화할 상품의 고유 ID입니다.
     */
    @Transactional
    public void removeProduct(final Long userId, final Long productId) {
        final Product product =
                productRepository
                        .findProductByIdAndUserIdAndIsUse(productId, userId, true)
                        .orElseThrow(NotFoundProductException::new);

        product.remove();
    }

    /**
     * 주어진 상품 이름에 비속어가 포함되어 있는지 검증합니다. 비속어가 포함되어 있을 경우 ShoppingBusinessException 예외를 발생시킵니다.
     *
     * @param name 검증할 상품 이름으로, 비속어 포함 여부를 확인합니다.
     */
    private void validProductNameProfanity(final String name) {
//        if (purgoMalumAdapter.isProfanity(name)) {
//            throw new ShoppingBusinessException("상품명에 비속어가 포함되어 있습니다.");
//        }
    }
}
