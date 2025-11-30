package tech.aerolambda.report.strategy.impl;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.ReportData;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvReportStrategy implements ReportStrategy {

    @Override
    public byte[] generate(List<Book> books) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {

            String[] header = {"ID", "Title", "ISBN", "Price", "Description", "Publication Year", "Author", "Store"};
            writer.writeNext(header);

            for (Book book : books) {
                String[] row = {
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getPrice() != null ? book.getPrice().toString() : "",
                        book.getDescription() != null ? book.getDescription() : "",
                        book.getPublicationYear() != null ? String.valueOf(book.getPublicationYear()) : "",
                        book.getAuthor() != null ? book.getAuthor().getName() : "",
                        book.getStore() != null ? book.getStore().getName() : ""
                };
                writer.writeNext(row);
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV report", e);
        }
    }

    @Override
    public <T> byte[] generate(ReportData<T> reportData) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {

            String[] header = reportData.getHeaders().toArray(new String[0]);
            writer.writeNext(header);

            for (T item : reportData.getData()) {
                List<String> rowData = reportData.getRowMapper().mapRow(item);
                String[] row = rowData.stream()
                        .map(s -> s != null ? s : "")
                        .toArray(String[]::new);
                writer.writeNext(row);
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV report", e);
        }
    }

    @Override
    public ReportFormat getFormat() {
        return ReportFormat.CSV;
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }
}
