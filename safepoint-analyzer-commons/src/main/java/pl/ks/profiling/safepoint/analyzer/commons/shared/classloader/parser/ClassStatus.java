package pl.ks.profiling.safepoint.analyzer.commons.shared.classloader.parser;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClassStatus {
    BigDecimal timeStamp;
    long loadedCount;
    long unloadedCount;

    public long getCurrentCount() {
        return loadedCount - unloadedCount;
    }
}