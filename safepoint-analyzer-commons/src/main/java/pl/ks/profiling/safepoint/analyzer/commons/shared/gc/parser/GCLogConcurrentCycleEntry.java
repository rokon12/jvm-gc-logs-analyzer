package pl.ks.profiling.safepoint.analyzer.commons.shared.gc.parser;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GCLogConcurrentCycleEntry {
    private long sequenceId;
    private BigDecimal time;

    public String getPhase() {
        return "Concurrent Mark";
    }
}