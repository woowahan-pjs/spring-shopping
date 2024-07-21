package shopping.category.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.category.api.dto.CategoryRegistrationHttpRequest;
import shopping.category.application.CategoryRegistrationUseCase;
import shopping.category.domain.Category;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;

import java.net.URI;

@RequestMapping("/api/categories")
@RestController
public class CategoryApi {

    private final CategoryRegistrationUseCase categoryRegistrationUseCase;

    public CategoryApi(final CategoryRegistrationUseCase categoryRegistrationUseCase) {
        this.categoryRegistrationUseCase = categoryRegistrationUseCase;
    }

    @PostMapping
    public ResponseEntity<?> register(
            @RequestBody final CategoryRegistrationHttpRequest request,
            @Authorization({AuthorizationType.ADMIN}) final AuthorizationUser admin
    ) {
        final Category category = categoryRegistrationUseCase.register(request.toCommand(admin.getUserId()));
        return ResponseEntity.created(URI.create("/api/categories/" + category.id())).build();
    }
}
