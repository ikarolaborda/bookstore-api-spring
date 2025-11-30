package tech.aerolambda.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.aerolambda.application.service.ReportService;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.domain.enums.ReportType;
import tech.aerolambda.presentation.dto.report.ReportRequest;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Report generation APIs")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/books/{format}")
    @Operation(summary = "Generate books report in specified format (PDF, CSV, XML, JSON)")
    public ResponseEntity<byte[]> generateBooksReport(
            @PathVariable ReportFormat format,
            @RequestParam(required = false) Integer limit) {
        byte[] reportData = reportService.generateBooksReport(format, limit);
        String contentType = reportService.getContentType(format);
        String fileExtension = reportService.getFileExtension(format);
        String filename = "books_report_" + LocalDate.now() + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(reportData);
    }

    @GetMapping("/{type}/{format}")
    @Operation(summary = "Generate report by type and format with optional filters")
    public ResponseEntity<byte[]> generateReport(
            @PathVariable ReportType type,
            @PathVariable ReportFormat format,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long authorId,
            Authentication authentication) {

        if (type == ReportType.USERS && !isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        ReportRequest request = ReportRequest.builder()
                .limit(limit)
                .startDate(startDate)
                .endDate(endDate)
                .authorId(authorId)
                .build();

        byte[] reportData = reportService.generateReport(type, format, request);
        String contentType = reportService.getContentType(format);
        String fileExtension = reportService.getFileExtension(format);
        String filename = type.name().toLowerCase() + "_report_" + LocalDate.now() + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(reportData);
    }

    @GetMapping("/types")
    @Operation(summary = "Get available report types based on user role")
    public ResponseEntity<ReportType[]> getAvailableReportTypes(Authentication authentication) {
        boolean isAdmin = isAdmin(authentication);
        return ResponseEntity.ok(reportService.getAvailableReportTypes(isAdmin));
    }

    @GetMapping("/formats")
    @Operation(summary = "Get available report formats")
    public ResponseEntity<ReportFormat[]> getAvailableFormats() {
        return ResponseEntity.ok(ReportFormat.values());
    }

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
