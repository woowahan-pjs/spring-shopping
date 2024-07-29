package shopping.customer.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;
import shopping.customer.infrastructure.api.dto.CustomerInfo;
import shopping.customer.infrastructure.api.dto.CustomerInfoHttpResponse;
import shopping.customer.infrastructure.persistence.CustomerDao;

@RequestMapping("/api/customers")
@RestController
public class CustomerQueryApi {

    private final CustomerDao customerDao;

    public CustomerQueryApi(final CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @GetMapping
    public ResponseEntity<CustomerInfoHttpResponse> myInfo(
            @Authorization({AuthorizationType.CUSTOMER}) final AuthorizationUser authorizationUser
    ) {
        CustomerInfo customerInfo = customerDao.findByCustomerId(authorizationUser.userId());
        return ResponseEntity.ok(new CustomerInfoHttpResponse(customerInfo.name()));
    }
}