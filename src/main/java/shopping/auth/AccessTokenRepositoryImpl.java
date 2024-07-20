package shopping.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Component
public class AccessTokenRepositoryImpl implements AccessTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public AccessTokenRepositoryImpl(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String create(final AuthorizationType authorizationType, final long id) {
        final String accessToken = UUID.randomUUID().toString();
        final String prefix = prefix(authorizationType);
        redisTemplate.opsForValue().set(prefix + accessToken, String.valueOf(id), Duration.ofHours(3));
        return accessToken;
    }

    @Override
    public long find(final AuthorizationType authorizationType, final String accessToken) {
        final String prefix = prefix(authorizationType);
        final String key = prefix + accessToken;
        final String value = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(value)) {
            throw new RuntimeException();
        }
        return Long.valueOf(value);
    }

    @Override
    public boolean exists(final AuthorizationType authorizationType, final String accessToken) {
        final String prefix = prefix(authorizationType);
        final String key = prefix + accessToken;
        final String value = redisTemplate.opsForValue().get(key);
        return Objects.nonNull(value);
    }

    private String prefix(final AuthorizationType authorizationType) {
        return switch (authorizationType) {
            case CUSTOMER -> "customer:";
            case SELLER -> "seller:";
            case ADMIN -> "admin:";
        };
    }
}
