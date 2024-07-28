package shopping.category.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.category.application.command.SubCategoryRegistrationCommand;
import shopping.category.application.query.CategoryQuery;
import shopping.category.domain.Category;
import shopping.category.domain.repository.CategoryRepository;
import shopping.utils.fake.FakeCategoryRepository;
import shopping.utils.fixture.CategoryFixture;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SubCategoryRegistrationUseCaseTest {
    private CategoryRepository categoryRepository;
    private SubCategoryRegistrationUseCase subCategoryRegistrationUseCase;

    @BeforeEach
    void setUp() {
        categoryRepository = new FakeCategoryRepository();
        subCategoryRegistrationUseCase = new CategoryService(categoryRepository);
    }

    @DisplayName("서브 카테고리를 등록할 수 있다.")
    @Test
    void register() {
        final Category category = categoryRepository.save(new Category(
                null,
                CategoryFixture.NAME,
                CategoryFixture.ORDER,
                new ArrayList<>(),
                CategoryFixture.ADMIN_ID,
                CategoryFixture.ADMIN_ID
        ));
        final SubCategoryRegistrationCommand subCategoryRegistrationCommand = new SubCategoryRegistrationCommand(
                CategoryFixture.SUB_NAME,
                CategoryFixture.SUB_ORDER,
                category.id(),
                CategoryFixture.OTHER_ADMIN_ID
        );
        final CategoryQuery categoryQuery = subCategoryRegistrationUseCase.registerSub(subCategoryRegistrationCommand);

        assertAll(
                () -> assertThat(categoryQuery).isNotNull(),
                () -> assertThat(categoryQuery.id()).isNotNull(),
                () -> assertThat(categoryQuery.name()).isEqualTo(CategoryFixture.NAME),
                () -> assertThat(categoryQuery.order()).isEqualTo(CategoryFixture.ORDER),
                () -> assertThat(categoryQuery.subCategories()).isNotEmpty(),
                () -> assertThat(categoryQuery.createdBy()).isEqualTo(CategoryFixture.ADMIN_ID),
                () -> assertThat(categoryQuery.modifiedBy()).isEqualTo(CategoryFixture.ADMIN_ID)
        );
    }
}
