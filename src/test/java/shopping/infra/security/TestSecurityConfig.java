package shopping.infra.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "shopping.infra.security")
public class TestSecurityConfig {}
