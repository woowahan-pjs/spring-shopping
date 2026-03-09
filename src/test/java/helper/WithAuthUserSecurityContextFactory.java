package helper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import shopping.infra.security.UserPrincipal;

class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        final UserPrincipal user =
                UserPrincipal.generate(1L, annotation.email(), annotation.role());

        final Authentication auth =
                new UsernamePasswordAuthenticationToken(user, "password", user.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
