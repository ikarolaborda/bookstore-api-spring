package tech.aerolambda.report.factory;

import org.springframework.stereotype.Component;
import tech.aerolambda.domain.enums.ReportFormat;
import tech.aerolambda.report.strategy.ReportStrategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportFactory {

    private final Map<ReportFormat, ReportStrategy> strategies;

    public ReportFactory(List<ReportStrategy> reportStrategies) {
        this.strategies = new EnumMap<>(ReportFormat.class);
        reportStrategies.forEach(strategy -> strategies.put(strategy.getFormat(), strategy));
    }

    public ReportStrategy getStrategy(ReportFormat format) {
        ReportStrategy strategy = strategies.get(format);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported report format: " + format);
        }
        return strategy;
    }

    public boolean supportsFormat(ReportFormat format) {
        return strategies.containsKey(format);
    }
}
