package shopping.utils.testcontainer;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainerExtension implements Extension, BeforeAllCallback {

    private static final String DOCKER_IMAGE = "redis:6.2.6";
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse(DOCKER_IMAGE));

    @Override
    public void beforeAll(final ExtensionContext extensionContext) throws Exception {
        if (REDIS_CONTAINER.isRunning()) {
            return;
        }
        REDIS_CONTAINER.start();
        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(REDIS_CONTAINER.getFirstMappedPort()));
    }
}
