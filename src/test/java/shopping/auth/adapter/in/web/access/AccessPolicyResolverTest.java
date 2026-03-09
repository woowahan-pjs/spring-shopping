package shopping.auth.adapter.in.web.access;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.adapter.in.web.annotation.MemberOnly;
import shopping.auth.adapter.in.web.annotation.SellerOnly;

@SpringJUnitConfig(AccessPolicyResolver.class)
class AccessPolicyResolverTest {
    @Autowired
    private AccessPolicyResolver resolver;

    @Test
    @DisplayName("접근 제어 애너테이션이 없으면 PUBLIC으로 본다")
    void resolvePublicWhenNoAnnotation() throws Exception {
        // given
        HandlerMethod handlerMethod = handlerMethod(new PublicController(), PublicController.class);

        // when
        AccessType accessType = resolver.resolve(handlerMethod);

        // then
        assertThat(accessType).isEqualTo(AccessType.PUBLIC);
    }

    @Test
    @DisplayName("클래스에 MemberOnly가 있으면 MEMBER로 본다")
    void resolveMemberWhenClassAnnotated() throws Exception {
        // given
        HandlerMethod handlerMethod = handlerMethod(new MemberController(), MemberController.class);

        // when
        AccessType accessType = resolver.resolve(handlerMethod);

        // then
        assertThat(accessType).isEqualTo(AccessType.MEMBER);
    }

    @Test
    @DisplayName("메서드에 MemberOnly가 있으면 MEMBER로 본다")
    void resolveMemberWhenMethodAnnotated() throws Exception {
        // given
        HandlerMethod handlerMethod = handlerMethod(new MethodMemberController(), MethodMemberController.class);

        // when
        AccessType accessType = resolver.resolve(handlerMethod);

        // then
        assertThat(accessType).isEqualTo(AccessType.MEMBER);
    }

    @Test
    @DisplayName("메서드에 SellerOnly가 있으면 SELLER로 본다")
    void resolveSellerWhenSellerAnnotationExists() throws Exception {
        // given
        HandlerMethod handlerMethod = handlerMethod(new SellerController(), SellerController.class);

        // when
        AccessType accessType = resolver.resolve(handlerMethod);

        // then
        assertThat(accessType).isEqualTo(AccessType.SELLER);
    }

    @Test
    @DisplayName("클래스는 MEMBER여도 메서드가 SELLER면 SELLER가 우선한다")
    void resolveSellerWhenClassMemberAndMethodSeller() throws Exception {
        // given
        HandlerMethod handlerMethod = handlerMethod(new MixedController(), MixedController.class);

        // when
        AccessType accessType = resolver.resolve(handlerMethod);

        // then
        assertThat(accessType).isEqualTo(AccessType.SELLER);
    }

    private HandlerMethod handlerMethod(Object controller, Class<?> controllerType) throws NoSuchMethodException {
        return new HandlerMethod(controller, controllerType.getMethod("endpoint"));
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
