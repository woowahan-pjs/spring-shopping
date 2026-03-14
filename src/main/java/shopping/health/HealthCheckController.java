package shopping.health;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final DataSource dataSource;

    public HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/liveness")
    public ResponseEntity<Map<String, String>> liveness() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        boolean mysqlReady = checkMysql();

        Map<String, Object> body =
                Map.of("status", mysqlReady ? "UP" : "DOWN", "mysql", mysqlReady ? "UP" : "DOWN");

        if (mysqlReady) {
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.status(503).body(body);
    }

    private boolean checkMysql() {
        try (var connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }
}
