package shopping;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import shopping.product.domain.Product;
import shopping.product.service.SaveProductService;

@AnalyzeClasses(packages = "shopping", importOptions = ImportOption.DoNotIncludeTests.class)
class ProductArchTest {

    @ArchTest
    static final ArchRule product_should_only_be_created_through_CreateProductService =
            classes().that().doNotHaveFullyQualifiedName(SaveProductService.class.getName())
                    .should(new ArchCondition<JavaClass>("not call Product constructor directly") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            javaClass.getConstructorCallsFromSelf().stream().filter(
                                    call -> call.getTargetOwner().isEquivalentTo(Product.class))
                                    .forEach(call -> events.add(SimpleConditionEvent.violated(call,
                                            javaClass.getName() + " calls Product constructor in "
                                                    + call.getSourceCodeLocation())));
                        }
                    });
}
