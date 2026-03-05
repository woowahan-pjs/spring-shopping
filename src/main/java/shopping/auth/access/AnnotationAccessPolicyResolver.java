package shopping.auth.access;

import java.lang.annotation.Annotation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.web.annotation.MemberOnly;
import shopping.auth.web.annotation.SellerOnly;

@Component
public class AnnotationAccessPolicyResolver implements AccessPolicyResolver {
    @Override
    public AccessType resolve(HandlerMethod handlerMethod) {
        if (hasAnnotation(handlerMethod, SellerOnly.class)) {
            return AccessType.SELLER;
        }
        if (hasAnnotation(handlerMethod, MemberOnly.class)) {
            return AccessType.MEMBER;
        }
        return AccessType.PUBLIC;
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class<? extends Annotation> annotationType) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), annotationType)
                || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), annotationType);
    }
}
