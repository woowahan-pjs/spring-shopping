package shopping.wishlist.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishList {
    @Builder.Default
    private List<Wish> wishList = new ArrayList<>();

    public int getTotCnt() {
        return wishList.size();
    }
}
