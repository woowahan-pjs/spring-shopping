package shopping.category.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.category.api.dto.CategoryRegistrationHttpRequest;
import shopping.category.api.dto.CategoryRegistrationHttpResponse;
import shopping.category.api.dto.SubCategoryRegistrationHttpRequest;
import shopping.category.api.dto.SubCategoryRegistrationHttpResponse;
import shopping.category.application.CategoryRegistrationUseCase;
import shopping.category.application.SubCategoryRegistrationUseCase;
import shopping.category.domain.Category;
import shopping.category.domain.SubCategory;
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
        final Category category = categoryRegistrationUseCase.register(request.toCommand(admin.userId()));
        return ResponseEntity.created(URI.create("/api/categories/" + category.id()))
                .body(new CategoryRegistrationHttpResponse(category.id()));
    }

    @PostMapping("/{categoryId}/sub")
    public ResponseEntity<SubCategoryRegistrationHttpResponse> registerSubCategory(
            @PathVariable(name = "categoryId") final long categoryId,
            @RequestBody final SubCategoryRegistrationHttpRequest request,
            @Authorization({AuthorizationType.ADMIN}) final AuthorizationUser admin
    ) {
        final Category category = subCategoryRegistrationUseCase.registerSub(request.toCommand(categoryId, admin.userId()));
        final List<SubCategory> subCategories = category.subCategories();
        final SubCategory subCategory = subCategories.getLast();
        return ResponseEntity.created(URI.create("/api/categories/" + category.id() + "/sub" + subCategory.getId()))
                .body(new SubCategoryRegistrationHttpResponse(subCategory.getId()));
    }
}
