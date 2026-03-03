package shopping.infra.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final Authentication authentication =
                    jwtTokenProvider.getAuthentication(extractTokenWithValid(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            authenticationEntryPoint.commence(request, response, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String uri = request.getRequestURI();

        return uri.startsWith("/swagger-ui")
                || uri.startsWith("/h2-console")
                || uri.startsWith("/v3/api-docs")
                || uri.equals("/favicon.ico");
    }

    private String extractTokenWithValid(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (ObjectUtils.isEmpty(authorization)) {
            throw new AuthenticationCredentialsNotFoundException("인증 정보가 없습니다.");
        }

        if (!authorization.startsWith(AUTHORIZATION_BEARER)) {
            throw new BadCredentialsException("잘못된 요청입니다.");
        }

        final String token = authorization.substring(AUTHORIZATION_BEARER.length());

        if (!jwtTokenProvider.isValidate(token)) {
            throw new BadCredentialsException("잘못된 요청입니다.");
        }

        return token;
    }
}
