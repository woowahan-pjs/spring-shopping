package shopping.product.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.wishlist.domain.Wish;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public int getTotCnt() {
        return products.size();
    }

    public boolean hasAnyExistPrdctNm(String prdctNm) {
        return products.stream()
                .anyMatch(p -> p.eqPrdctNm(prdctNm));
    }
}
