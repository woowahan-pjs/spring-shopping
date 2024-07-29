package shopping.category.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.category.application.command.CategoryRegistrationCommand;
import shopping.category.application.query.CategoryQuery;
import shopping.category.domain.repository.CategoryRepository;
import shopping.utils.fake.FakeCategoryRepository;
import shopping.utils.fixture.CategoryFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CategoryRegistrationUseCaseTest {

    private CategoryRepository categoryRepository;
    private CategoryRegistrationUseCase categoryRegistrationUseCase;

    @BeforeEach
    void setUp() {
        categoryRepository = new FakeCategoryRepository();
        categoryRegistrationUseCase = new CategoryService(categoryRepository);
    }

    @DisplayName("카테고리를 등록할 수 있다.")
    @Test
    void register() {
        final CategoryRegistrationCommand categoryRegistrationCommand = new CategoryRegistrationCommand(
                CategoryFixture.NAME,
                CategoryFixture.ORDER,
                CategoryFixture.ADMIN_ID
        );
        final CategoryQuery categoryQuery = categoryRegistrationUseCase.register(categoryRegistrationCommand);

        assertAll(
                () -> assertThat(categoryQuery).isNotNull(),
                () -> assertThat(categoryQuery.id()).isNotNull(),
                () -> assertThat(categoryQuery.name()).isEqualTo(CategoryFixture.NAME),
                () -> assertThat(categoryQuery.order()).isEqualTo(CategoryFixture.ORDER),
                () -> assertThat(categoryQuery.createdBy()).isEqualTo(CategoryFixture.ADMIN_ID),
                () -> assertThat(categoryQuery.modifiedBy()).isEqualTo(CategoryFixture.ADMIN_ID)
        );
    }
}
