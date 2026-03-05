package shopping.auth.web;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shopping.auth.web.annotation.CurrentMemberId;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Component
public class MemberIdResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(CurrentMemberId.class)) {
            return false;
        }
        Class<?> parameterType = parameter.getParameterType();
        return Long.class.equals(parameterType) || long.class.equals(parameterType);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Object memberId = webRequest.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (memberId instanceof Long) {
            return memberId;
        }
        throw new ApiException(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }
}
