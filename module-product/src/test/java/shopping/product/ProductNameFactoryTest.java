package shopping.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductNameFactoryTest {

    private ProductNameFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProductNameFactory(new FakeProfanityChecker("badword"));
    }

    @Test
    void 유효한_이름으로_생성한다() {
        ProductName name = factory.create("상품이름");

        assertEquals("상품이름", name.getValue());
        assertTrue(name.isVerified());
    }

    @Test
    void 영문_숫자_한글_공백_괄호_하이픈_플러스를_허용한다() {
        ProductName name = factory.create("Product (1-A)+");

        assertEquals("Product (1-A)+", name.getValue());
        assertTrue(name.isVerified());
    }

    @Test
    void null이면_예외가_발생한다() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create(null));

        assertEquals("상품 이름은 필수입니다.", exception.getMessage());
    }

    @Test
    void 빈_문자열이면_예외가_발생한다() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create(""));

        assertEquals("상품 이름은 필수입니다.", exception.getMessage());
    }

    @Test
    void 공백만_있으면_예외가_발생한다() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create("   "));

        assertEquals("상품 이름은 필수입니다.", exception.getMessage());
    }

    @Test
    void 이름이_15자를_초과하면_예외가_발생한다() {
        String longName = "a".repeat(16);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create(longName));

        assertEquals("상품 이름은 공백 포함 15자 이하여야 합니다.", exception.getMessage());
    }

    @Test
    void 이름이_15자이면_성공한다() {
        ProductName name = factory.create("a".repeat(15));

        assertEquals("a".repeat(15), name.getValue());
    }

    @Test
    void 허용되지_않는_특수문자가_포함되면_예외가_발생한다() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create("상품@이름"));

        assertEquals("상품 이름에 허용되지 않는 특수문자가 포함되어 있습니다.", exception.getMessage());
    }

    @Test
    void 비속어가_포함되면_예외가_발생한다() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> factory.create("badword"));

        assertEquals("상품 이름에 비속어가 포함되어 있습니다.", exception.getMessage());
    }

    @Test
    void 미인증_이름을_생성한다() {
        ProductName name = factory.createUnverified("상품");

        assertEquals("상품", name.getValue());
        assertFalse(name.isVerified());
    }
}
