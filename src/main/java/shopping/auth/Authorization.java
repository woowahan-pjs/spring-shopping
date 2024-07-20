package shopping.auth;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface Authorization {
    AuthorizationType[] value();
}
