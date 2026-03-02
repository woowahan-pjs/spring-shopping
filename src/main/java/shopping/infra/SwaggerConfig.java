package shopping.infra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

// TODO: security 설정 추가 필요
@OpenAPIDefinition(
        info = @Info(title = "Shopping API", description = "[과제4] Shopping API 문서"),
        servers = {@Server(url = "http://localhost:8080", description = "local")})
class SwaggerConfig {}
