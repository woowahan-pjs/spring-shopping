package shopping.seller.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.seller.infrastructure.api.dto.SellerInfo;
import shopping.seller.infrastructure.api.dto.SellerInfoHttpResponse;
import shopping.seller.infrastructure.persistence.SellerDao;

@RequestMapping("/api/sellers")
@RestController
public class SellerQueryApi {
    private final SellerDao sellerDao;

    public SellerQueryApi(final SellerDao sellerDao) {
        this.sellerDao = sellerDao;
    }

    @GetMapping
    public ResponseEntity<SellerInfoHttpResponse> getSellerInfo(
            @Authorization({AuthorizationType.SELLER}) AuthorizationUser authorizationUser
    ) {
        final SellerInfo sellerInfo = sellerDao.getSellerInfo(authorizationUser.userId());
        return ResponseEntity.ok(new SellerInfoHttpResponse(sellerInfo.name()));
    }
}
