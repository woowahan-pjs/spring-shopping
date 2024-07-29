package shopping.category.infrastrcuture.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.category.application.CategoryRegistrationUseCase;
import shopping.category.application.SubCategoryRegistrationUseCase;
import shopping.category.application.query.CategoryQuery;
import shopping.category.application.query.SubCategoryQuery;
import shopping.category.infrastrcuture.api.dto.CategoryRegistrationHttpRequest;
import shopping.category.infrastrcuture.api.dto.CategoryRegistrationHttpResponse;
import shopping.category.infrastrcuture.api.dto.SubCategoryRegistrationHttpRequest;
import shopping.category.infrastrcuture.api.dto.SubCategoryRegistrationHttpResponse;
import shopping.common.auth.Authorization;
import shopping.common.auth.AuthorizationType;
import shopping.common.auth.AuthorizationUser;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/categories")
@RestController
public class CategoryApi {

    private final CategoryRegistrationUseCase categoryRegistrationUseCase;
    private final SubCategoryRegistrationUseCase subCategoryRegistrationUseCase;

    public CategoryApi(final CategoryRegistrationUseCase categoryRegistrationUseCase, final SubCategoryRegistrationUseCase subCategoryRegistrationUseCase) {
        this.categoryRegistrationUseCase = categoryRegistrationUseCase;
        this.subCategoryRegistrationUseCase = subCategoryRegistrationUseCase;
    }

    @PostMapping
    public ResponseEntity<CategoryRegistrationHttpResponse> register(
            @RequestBody final CategoryRegistrationHttpRequest request,
            @Authorization({AuthorizationType.ADMIN}) final AuthorizationUser admin
    ) {
        final CategoryQuery categoryQuery = categoryRegistrationUseCase.register(request.toCommand(admin.userId()));
        return ResponseEntity.created(URI.create("/api/categories/" + categoryQuery.id()))
                .body(new CategoryRegistrationHttpResponse(categoryQuery.id()));
    }

    @PostMapping("/{categoryId}/sub")
    public ResponseEntity<SubCategoryRegistrationHttpResponse> registerSubCategory(
            @PathVariable(name = "categoryId") final long categoryId,
            @RequestBody final SubCategoryRegistrationHttpRequest request,
            @Authorization({AuthorizationType.ADMIN}) final AuthorizationUser admin
    ) {
        final CategoryQuery categoryQuery = subCategoryRegistrationUseCase.registerSub(request.toCommand(categoryId, admin.userId()));
        final List<SubCategoryQuery> subCategories = categoryQuery.subCategories();
        final SubCategoryQuery subCategoryQuery = subCategories.getLast();
        return ResponseEntity.created(URI.create("/api/categories/" + categoryQuery.id() + "/sub" + subCategoryQuery.id()))
                .body(new SubCategoryRegistrationHttpResponse(subCategoryQuery.id()));
    }
}
