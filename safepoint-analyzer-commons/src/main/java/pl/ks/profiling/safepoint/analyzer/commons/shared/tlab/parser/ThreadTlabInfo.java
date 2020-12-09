package pl.ks.profiling.safepoint.analyzer.commons.shared.tlab.parser;

import lombok.Builder;
import lombok.Value;
import pl.ks.profiling.safepoint.analyzer.commons.shared.OneFiledAllStats;

@Value
@Builder
public class ThreadTlabInfo {
    String tid;
    long nid;
    OneFiledAllStats size;
    OneFiledAllStats slowAllocs;
}