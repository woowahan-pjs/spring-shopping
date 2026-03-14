package shopping.health;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "server.health-port.enabled", havingValue = "true",
        matchIfMissing = true)
public class HealthCheckPortFilter extends OncePerRequestFilter {

    private final int healthPort;

    public HealthCheckPortFilter(@Value("${server.health-port:8081}") int healthPort) {
        this.healthPort = healthPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        boolean isHealthPort = request.getLocalPort() == healthPort;
        boolean isHealthPath = request.getServletPath().startsWith("/health");

        if (isHealthPort && !isHealthPath) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!isHealthPort && isHealthPath) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
