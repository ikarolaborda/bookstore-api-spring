package tech.aerolambda.report.strategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.ReportData;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JsonReportStrategy implements ReportStrategy {

    private final ObjectMapper objectMapper;

    public JsonReportStrategy() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public byte[] generate(List<Book> books) {
        try {
            Map<String, Object> report = new HashMap<>();

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("totalBooks", books.size());
            metadata.put("generatedAt", LocalDateTime.now().toString());
            report.put("metadata", metadata);

            List<Map<String, Object>> booksData = books.stream()
                    .map(this::mapBook)
                    .collect(Collectors.toList());
            report.put("books", booksData);

            return objectMapper.writeValueAsBytes(report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON report", e);
        }
    }

    @Override
    public <T> byte[] generate(ReportData<T> reportData) {
        try {
            Map<String, Object> report = new LinkedHashMap<>();

            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("title", reportData.getTitle());
            metadata.put("totalRecords", reportData.getData().size());
            metadata.put("generatedAt", LocalDateTime.now().toString());
            report.put("metadata", metadata);

            List<Map<String, String>> records = new ArrayList<>();
            List<String> headers = reportData.getHeaders();

            for (T item : reportData.getData()) {
                Map<String, String> record = new LinkedHashMap<>();
                List<String> rowData = reportData.getRowMapper().mapRow(item);
                for (int i = 0; i < headers.size(); i++) {
                    String key = headers.get(i).toLowerCase().replace(" ", "_");
                    String value = i < rowData.size() ? rowData.get(i) : "";
                    record.put(key, value);
                }
                records.add(record);
            }
            report.put("records", records);

            return objectMapper.writeValueAsBytes(report);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON report", e);
        }
    }

    private Map<String, Object> mapBook(Book book) {
        Map<String, Object> bookMap = new HashMap<>();
        bookMap.put("id", book.getId());
        bookMap.put("title", book.getTitle());
        bookMap.put("isbn", book.getIsbn());
        bookMap.put("price", book.getPrice());
        bookMap.put("description", book.getDescription());
        bookMap.put("publicationYear", book.getPublicationYear());

        if (book.getAuthor() != null) {
            Map<String, Object> authorMap = new HashMap<>();
            authorMap.put("id", book.getAuthor().getId());
            authorMap.put("name", book.getAuthor().getName());
            bookMap.put("author", authorMap);
        }

        if (book.getStore() != null) {
            Map<String, Object> storeMap = new HashMap<>();
            storeMap.put("id", book.getStore().getId());
            storeMap.put("name", book.getStore().getName());
            bookMap.put("store", storeMap);
        }

        return bookMap;
    }

    @Override
    public ReportFormat getFormat() {
        return ReportFormat.JSON;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }
}
