package tech.aerolambda.presentation.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private Integer limit;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long authorId;
}
