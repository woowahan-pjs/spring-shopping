package shopping.auth.adapter.in.web.access;

import java.lang.annotation.Annotation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.adapter.in.web.annotation.MemberOnly;
import shopping.auth.adapter.in.web.annotation.SellerOnly;

@Component
public class AccessPolicyResolver {
    public AccessType resolve(HandlerMethod controllerMethod) {
        if (isSellerOnlyEndpoint(controllerMethod)) {
            return AccessType.SELLER;
        }
        if (isMemberOnlyEndpoint(controllerMethod)) {
            return AccessType.MEMBER;
        }
        return AccessType.PUBLIC;
    }

    private boolean isSellerOnlyEndpoint(HandlerMethod controllerMethod) {
        return hasMethodOrTypeAnnotation(controllerMethod, SellerOnly.class);
    }

    private boolean isMemberOnlyEndpoint(HandlerMethod controllerMethod) {
        return hasMethodOrTypeAnnotation(controllerMethod, MemberOnly.class);
    }

    private boolean hasMethodOrTypeAnnotation(
            HandlerMethod controllerMethod,
            Class<? extends Annotation> annotationType
    ) {
        return AnnotatedElementUtils.hasAnnotation(controllerMethod.getMethod(), annotationType)
                || AnnotatedElementUtils.hasAnnotation(controllerMethod.getBeanType(), annotationType);
    }
}
