package shopping.health;

import java.net.InetAddress;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "server.health-port.enabled", havingValue = "true",
        matchIfMissing = true)
public class HealthCheckConnectorConfig {

    @Value("${server.health-port:8081}")
    private int healthPort;

    @Value("${server.address:}")
    private String serverAddress;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> healthCheckConnector() {
        return factory -> factory.addAdditionalTomcatConnectors(createHealthCheckConnector());
    }

    private Connector createHealthCheckConnector() {
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(healthPort);

        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

        if (!serverAddress.isEmpty()) {
            try {
                protocol.setAddress(InetAddress.getByName(serverAddress));
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Invalid server.address for health connector: " + serverAddress, e);
            }
        }

        protocol.setMaxThreads(5);
        protocol.setMinSpareThreads(2);
        protocol.setAcceptCount(10);
        protocol.setConnectionTimeout(5000);

        return connector;
    }
}
