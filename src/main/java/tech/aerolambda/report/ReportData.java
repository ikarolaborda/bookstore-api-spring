package tech.aerolambda.report;

import lombok.Builder;
import lombok.Getter;
import tech.aerolambda.domain.enums.ReportType;

import java.util.List;

@Getter
@Builder
public class ReportData<T> {
    private final ReportType reportType;
    private final String title;
    private final List<String> headers;
    private final List<T> data;
    private final RowMapper<T> rowMapper;

    @FunctionalInterface
    public interface RowMapper<T> {
        List<String> mapRow(T item);
    }
}
