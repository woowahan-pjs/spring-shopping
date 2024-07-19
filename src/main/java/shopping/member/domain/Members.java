package shopping.member.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.product.domain.Product;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Members {
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    public int getTotCnt() {
        return members.size();
    }
}
