package tech.aerolambda.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.aerolambda.domain.repository.AuthorRepository;
import tech.aerolambda.domain.repository.BookRepository;
import tech.aerolambda.domain.repository.StoreRepository;
import tech.aerolambda.domain.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
@Tag(name = "Database", description = "Database information endpoints")
public class DatabaseController {

    private final DataSource dataSource;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get database information", description = "Returns database connection info and entity counts")
    public ResponseEntity<Map<String, Object>> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Map<String, String> connectionInfo = new HashMap<>();
            connectionInfo.put("databaseProductName", metaData.getDatabaseProductName());
            connectionInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            connectionInfo.put("driverName", metaData.getDriverName());
            connectionInfo.put("driverVersion", metaData.getDriverVersion());
            connectionInfo.put("url", metaData.getURL());
            connectionInfo.put("userName", metaData.getUserName());

            info.put("connection", connectionInfo);
        } catch (SQLException e) {
            info.put("connection", Map.of("error", e.getMessage()));
        }

        Map<String, Long> counts = new HashMap<>();
        counts.put("books", bookRepository.count());
        counts.put("authors", authorRepository.count());
        counts.put("stores", storeRepository.count());
        counts.put("users", userRepository.count());

        info.put("entityCounts", counts);
        info.put("status", "connected");

        return ResponseEntity.ok(info);
    }
}
