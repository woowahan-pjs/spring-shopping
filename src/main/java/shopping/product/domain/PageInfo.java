package shopping.product.domain;

import org.springframework.data.domain.Pageable;

public record PageInfo(
    int page,
    int size,
    long offset,
    boolean paged,
    boolean unpaged
) {
    public static PageInfo from(final Pageable pageable) {
        return new PageInfo(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getOffset(),
            pageable.isPaged(),
            pageable.isUnpaged()
        );
    }
}
