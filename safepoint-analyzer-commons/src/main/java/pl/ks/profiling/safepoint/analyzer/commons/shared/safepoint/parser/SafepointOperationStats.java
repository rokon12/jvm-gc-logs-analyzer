package pl.ks.profiling.safepoint.analyzer.commons.shared.safepoint.parser;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import pl.ks.profiling.safepoint.analyzer.commons.shared.OneFiledAllStats;

@Getter
@Setter
public class SafepointOperationStats {
    private Long id;
    private long totalCount;
    private OneFiledAllStats tts;
    private OneFiledAllStats operationTime;
    private OneFiledAllStats applicationTime;
    private Set<SafepointOperationStatsByName> statsByNames;
    private List<TimesInTime> timesInTimes2sec;
    private List<TimesInTime> timesInTimes5sec;
    private List<TimesInTime> timesInTimes15sec;
}