package tech.aerolambda.application.service;

import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.domain.enums.ReportType;
import tech.aerolambda.presentation.dto.report.ReportRequest;

public interface ReportService {

    byte[] generateBooksReport(ReportFormat format, Integer limit);

    byte[] generateReport(ReportType type, ReportFormat format, ReportRequest request);

    String getContentType(ReportFormat format);

    String getFileExtension(ReportFormat format);

    ReportType[] getAvailableReportTypes(boolean isAdmin);
}
