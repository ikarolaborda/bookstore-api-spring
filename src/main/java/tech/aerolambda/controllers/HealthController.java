package tech.aerolambda.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    record Health(String status, long uptimeSeconds) {}
    record Database(String status) {}

    @GetMapping("/health")
    public Health healthCheck() {
        return new Health(
                "UP",
                ManagementFactory.getRuntimeMXBean().getUptime() / 1000
        );
    }

    @GetMapping("/version")
    public String version() {
        return "1.0.0";
    }

    @GetMapping(value = "/database", produces = "application/json")
    public Database database() {
        try {
            dataSource.getConnection().close();
            return new Database("DB is connected");
        } catch (SQLException e) {
            return new Database("Not connected to database: " + e.getMessage());
        }
    }
}

