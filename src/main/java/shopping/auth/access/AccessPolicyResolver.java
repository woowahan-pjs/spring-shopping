package shopping.auth.access;

import org.springframework.web.method.HandlerMethod;

public interface AccessPolicyResolver {
    AccessType resolve(HandlerMethod handlerMethod);
}
