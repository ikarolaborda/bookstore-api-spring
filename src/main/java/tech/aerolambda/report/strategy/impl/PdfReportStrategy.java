package tech.aerolambda.report.strategy.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.ReportData;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class PdfReportStrategy implements ReportStrategy {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font CELL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);

    @Override
    public byte[] generate(List<Book> books) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Paragraph title = new Paragraph("Books Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 1.5f, 2, 2});

            addTableHeader(table);
            addTableRows(table, books);

            document.add(table);

            Paragraph footer = new Paragraph(
                    String.format("\nTotal books: %d | Generated on: %s",
                            books.size(),
                            java.time.LocalDateTime.now().toString()),
                    CELL_FONT);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (DocumentException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    @Override
    public <T> byte[] generate(ReportData<T> reportData) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Paragraph title = new Paragraph(reportData.getTitle(), TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            List<String> headers = reportData.getHeaders();
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            addGenericTableHeader(table, headers);
            addGenericTableRows(table, reportData);

            document.add(table);

            Paragraph footer = new Paragraph(
                    String.format("\nTotal records: %d | Generated on: %s",
                            reportData.getData().size(),
                            java.time.LocalDateTime.now().toString()),
                    CELL_FONT);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (DocumentException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Title", "ISBN", "Price", "Author", "Store")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setPadding(5);
                    table.addCell(header);
                });
    }

    private void addGenericTableHeader(PdfPTable table, List<String> headers) {
        headers.forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(5);
            table.addCell(header);
        });
    }

    private void addTableRows(PdfPTable table, List<Book> books) {
        for (Book book : books) {
            addCell(table, String.valueOf(book.getId()));
            addCell(table, book.getTitle());
            addCell(table, book.getIsbn());
            addCell(table, book.getPrice() != null ? "$" + book.getPrice().toString() : "N/A");
            addCell(table, book.getAuthor() != null ? book.getAuthor().getName() : "N/A");
            addCell(table, book.getStore() != null ? book.getStore().getName() : "N/A");
        }
    }

    private <T> void addGenericTableRows(PdfPTable table, ReportData<T> reportData) {
        for (T item : reportData.getData()) {
            List<String> rowData = reportData.getRowMapper().mapRow(item);
            rowData.forEach(cellValue -> addCell(table, cellValue != null ? cellValue : "N/A"));
        }
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
        cell.setPadding(4);
        table.addCell(cell);
    }

    @Override
    public ReportFormat getFormat() {
        return ReportFormat.PDF;
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }
}
