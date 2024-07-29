package shopping.wishlist.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WishListInfoHttpResponse {

    @JsonProperty("wish-lists")
    private List<WishListInfo> wishListInfos;

    public WishListInfoHttpResponse() {
    }

    public WishListInfoHttpResponse(final List<WishListInfo> wishListInfos) {
        this.wishListInfos = wishListInfos;
    }
}
