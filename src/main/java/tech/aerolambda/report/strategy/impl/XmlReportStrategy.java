package tech.aerolambda.report.strategy.impl;

import org.springframework.stereotype.Component;
import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.ReportData;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class XmlReportStrategy implements ReportStrategy {

    @Override
    public byte[] generate(List<Book> books) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<booksReport>\n");
        xml.append("  <metadata>\n");
        xml.append("    <totalBooks>").append(books.size()).append("</totalBooks>\n");
        xml.append("    <generatedAt>").append(java.time.LocalDateTime.now()).append("</generatedAt>\n");
        xml.append("  </metadata>\n");
        xml.append("  <books>\n");

        for (Book book : books) {
            xml.append("    <book>\n");
            xml.append("      <id>").append(book.getId()).append("</id>\n");
            xml.append("      <title>").append(escapeXml(book.getTitle())).append("</title>\n");
            xml.append("      <isbn>").append(escapeXml(book.getIsbn())).append("</isbn>\n");
            xml.append("      <price>").append(book.getPrice() != null ? book.getPrice() : "").append("</price>\n");
            xml.append("      <description>").append(escapeXml(book.getDescription())).append("</description>\n");
            xml.append("      <publicationYear>").append(book.getPublicationYear() != null ? book.getPublicationYear() : "").append("</publicationYear>\n");
            xml.append("      <author>\n");
            if (book.getAuthor() != null) {
                xml.append("        <id>").append(book.getAuthor().getId()).append("</id>\n");
                xml.append("        <name>").append(escapeXml(book.getAuthor().getName())).append("</name>\n");
            }
            xml.append("      </author>\n");
            xml.append("      <store>\n");
            if (book.getStore() != null) {
                xml.append("        <id>").append(book.getStore().getId()).append("</id>\n");
                xml.append("        <name>").append(escapeXml(book.getStore().getName())).append("</name>\n");
            }
            xml.append("      </store>\n");
            xml.append("    </book>\n");
        }

        xml.append("  </books>\n");
        xml.append("</booksReport>");

        return xml.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> byte[] generate(ReportData<T> reportData) {
        StringBuilder xml = new StringBuilder();
        String rootElement = reportData.getReportType().name().toLowerCase() + "Report";
        String itemElement = reportData.getReportType().name().toLowerCase().replace("_", "");
        if (itemElement.endsWith("s")) {
            itemElement = itemElement.substring(0, itemElement.length() - 1);
        }

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<").append(rootElement).append(">\n");
        xml.append("  <metadata>\n");
        xml.append("    <title>").append(escapeXml(reportData.getTitle())).append("</title>\n");
        xml.append("    <totalRecords>").append(reportData.getData().size()).append("</totalRecords>\n");
        xml.append("    <generatedAt>").append(java.time.LocalDateTime.now()).append("</generatedAt>\n");
        xml.append("  </metadata>\n");
        xml.append("  <records>\n");

        List<String> headers = reportData.getHeaders();
        for (T item : reportData.getData()) {
            xml.append("    <").append(itemElement).append(">\n");
            List<String> rowData = reportData.getRowMapper().mapRow(item);
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i).toLowerCase().replace(" ", "_");
                String value = i < rowData.size() ? rowData.get(i) : "";
                xml.append("      <").append(header).append(">")
                        .append(escapeXml(value))
                        .append("</").append(header).append(">\n");
            }
            xml.append("    </").append(itemElement).append(">\n");
        }

        xml.append("  </records>\n");
        xml.append("</").append(rootElement).append(">");

        return xml.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    @Override
    public ReportFormat getFormat() {
        return ReportFormat.XML;
    }

    @Override
    public String getContentType() {
        return "application/xml";
    }

    @Override
    public String getFileExtension() {
        return ".xml";
    }
}
