package pl.ks.profiling.safepoint.analyzer.commons.shared.gc.parser;

import lombok.Getter;
import lombok.Setter;
import pl.ks.profiling.safepoint.analyzer.commons.shared.OneFiledAllStats;

@Getter
@Setter
public class GCConcurrentCycleStats {
    private String name;
    private Long count;
    private OneFiledAllStats time;
}