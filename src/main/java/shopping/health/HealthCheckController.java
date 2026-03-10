package shopping.health;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;

    public HealthCheckController(DataSource dataSource, MongoTemplate mongoTemplate) {
        this.dataSource = dataSource;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/liveness")
    public ResponseEntity<Map<String, String>> liveness() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        boolean mysqlReady = checkMysql();
        boolean mongoReady = checkMongo();
        boolean allReady = mysqlReady && mongoReady;

        Map<String, Object> body = Map.of("status", allReady ? "UP" : "DOWN", "mysql",
                mysqlReady ? "UP" : "DOWN", "mongo", mongoReady ? "UP" : "DOWN");

        if (allReady) {
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

    private boolean checkMongo() {
        try {
            mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
