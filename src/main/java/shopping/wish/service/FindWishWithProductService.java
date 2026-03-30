package shopping.wish.service;

import shopping.member.domain.MemberRepository;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductStatus;
import shopping.product.domain.Product;
import shopping.wish.domain.Wish;
import shopping.wish.dto.WishResponse;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FindWishWithProductService {

    private static final Logger log = LoggerFactory.getLogger(FindWishWithProductService.class);

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public FindWishWithProductService(MemberRepository memberRepository,
            ProductRepository productRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<WishResponse> execute(UUID memberId) {
        List<Wish> wishes = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다.")).getWishes();

        List<UUID> productIds = wishes.stream().map(Wish::getProductId).toList();
        Map<UUID, Product> productMap =
                productRepository.findAllByIdInAndStatus(productIds, ProductStatus.CREATED).stream()
                        .collect(Collectors.toMap(Product::getId, Function.identity()));

        return wishes.stream().filter(wish -> {
            if (!productMap.containsKey(wish.getProductId())) {
                log.warn("Wish {} references non-existent product {}", wish.getId(),
                        wish.getProductId());
                return false;
            }
            return true;
        }).map(wish -> {
            Product product = productMap.get(wish.getProductId());
            return new WishResponse(product.getId(), product.getName().getValue(),
                    product.getPrice(), product.getImageUrl());
        }).toList();
    }
}
