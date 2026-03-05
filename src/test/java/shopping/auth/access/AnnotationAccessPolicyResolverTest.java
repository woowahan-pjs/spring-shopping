package shopping.auth.access;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.web.annotation.MemberOnly;
import shopping.auth.web.annotation.SellerOnly;

class AnnotationAccessPolicyResolverTest {
    private final AnnotationAccessPolicyResolver resolver = new AnnotationAccessPolicyResolver();

    @Test
    void resolvePublicWhenNoAnnotation() throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(new PublicController(), PublicController.class.getMethod("endpoint"));

        AccessType accessType = resolver.resolve(handlerMethod);

        assertThat(accessType).isEqualTo(AccessType.PUBLIC);
    }

    @Test
    void resolveMemberWhenClassAnnotated() throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(new MemberController(), MemberController.class.getMethod("endpoint"));

        AccessType accessType = resolver.resolve(handlerMethod);

        assertThat(accessType).isEqualTo(AccessType.MEMBER);
    }

    @Test
    void resolveMemberWhenMethodAnnotated() throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(new MethodMemberController(), MethodMemberController.class.getMethod("endpoint"));

        AccessType accessType = resolver.resolve(handlerMethod);

        assertThat(accessType).isEqualTo(AccessType.MEMBER);
    }

    @Test
    void resolveSellerWhenSellerAnnotationExists() throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(new SellerController(), SellerController.class.getMethod("endpoint"));

        AccessType accessType = resolver.resolve(handlerMethod);

        assertThat(accessType).isEqualTo(AccessType.SELLER);
    }

    @Test
    void resolveSellerWhenClassMemberAndMethodSeller() throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(new MixedController(), MixedController.class.getMethod("endpoint"));

        AccessType accessType = resolver.resolve(handlerMethod);

        assertThat(accessType).isEqualTo(AccessType.SELLER);
    }

    static class PublicController {
        public void endpoint() {
        }
    }

    @MemberOnly
    static class MemberController {
        public void endpoint() {
        }
    }

    static class MethodMemberController {
        @MemberOnly
        public void endpoint() {
        }
    }

    static class SellerController {
        @SellerOnly
        public void endpoint() {
        }
    }

    @MemberOnly
    static class MixedController {
        @SellerOnly
        public void endpoint() {
        }
    }
}
