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
import shopping.product.domain.ProductName;
import shopping.product.domain.ProductNameFactory;

@AnalyzeClasses(packages = "shopping", importOptions = ImportOption.DoNotIncludeTests.class)
class ProductNameArchTest {

    @ArchTest
    static final ArchRule productName_constructor_should_only_be_called_by_factory =
            classes().that().doNotHaveFullyQualifiedName(ProductNameFactory.class.getName()).should(
                    new ArchCondition<JavaClass>("not call ProductName constructor directly") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            javaClass.getConstructorCallsFromSelf().stream().filter(
                                    call -> call.getTargetOwner().isEquivalentTo(ProductName.class))
                                    .forEach(call -> events.add(SimpleConditionEvent.violated(call,
                                            javaClass.getName()
                                                    + " calls ProductName constructor in "
                                                    + call.getSourceCodeLocation())));
                        }
                    });
}
