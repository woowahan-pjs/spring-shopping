package shopping.member.client.applicaton.dto;

import java.math.BigDecimal;

public record WishProductResponse(
        Long id,
        String name,
        BigDecimal bigDecimal,
        String image
) {

}
