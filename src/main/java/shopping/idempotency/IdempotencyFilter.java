package shopping.idempotency;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyKeyRepository repository;

    public IdempotencyFilter(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equalsIgnoreCase(request.getMethod())
                || request.getHeader("Idempotency-Key") == null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String key = request.getHeader("Idempotency-Key");
        IdempotencyKey entry = new IdempotencyKey(key);
        var existing = repository.putIfAbsent(entry);

        if (existing.isPresent()) {
            sendConflict(existing.get(), response);
            return;
        }

        try {
            filterChain.doFilter(request, response);
            if (response.getStatus() >= 500) {
                repository.remove(entry.getKey());
            } else {
                entry.complete();
                repository.save(entry);
            }
        } catch (Exception e) {
            repository.remove(entry.getKey());
            throw e;
        }
    }

    private void sendConflict(IdempotencyKey entry, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        response.setContentType("application/json;charset=UTF-8");
        switch (entry.getStatus()) {
            case PENDING -> response.getWriter().write("{\"message\":\"요청이 처리 중입니다.\"}");
            case COMPLETED -> response.getWriter().write("{\"message\":\"이미 처리된 요청입니다.\"}");
        }
    }
}
