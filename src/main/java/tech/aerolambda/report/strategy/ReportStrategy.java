package tech.aerolambda.report.strategy;

import tech.aerolambda.domain.entity.Book;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.ReportData;

import java.util.List;

public interface ReportStrategy {

    byte[] generate(List<Book> books);

    <T> byte[] generate(ReportData<T> reportData);

    ReportFormat getFormat();

    String getContentType();

    String getFileExtension();
}
