/*
 * Copyright 2020 Krzysztof Slusarski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.ks.profiling.safepoint.analyzer.commons.shared.gc.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class GCLogFile {
    @Getter
    private List<GCLogCycleEntry> cycleEntries = new ArrayList<>();
    @Getter
    private List<GCLogConcurrentCycleEntry> concurrentCycleEntries = new ArrayList<>();
    @Getter
    private Map<Long, List<String>> rawLogLines = new HashMap<>();
    @Getter
    private GCStats stats;

    private Map<Long, GCLogCycleEntry> unprocessedCycles = new HashMap<>();


    void newLine(Long cycleId, String line) {
        rawLogLines.computeIfAbsent(cycleId, id -> new ArrayList<>()).add(line);
    }

    void newPhase(Long sequenceId, String phase, BigDecimal timeStamp) {
        unprocessedCycles.put(sequenceId, new GCLogCycleEntry(sequenceId, phase, timeStamp));
    }

    void addSubPhaseTime(Long sequenceId, String phase, BigDecimal time) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addSubPhaseTime(phase, time);
    }

    void addSizesAndTime(Long sequenceId, int heapBeforeGC, int heapAfterGC, int heapSize, BigDecimal phaseTime) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.remove(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        cycleEntries.add(gcLogCycleEntry);
        gcLogCycleEntry.addSizesAndTime(heapBeforeGC, heapAfterGC, heapSize, phaseTime);
    }

    void addRegionCount(Long sequenceId, String regionName, Integer regionsBeforeGC, Integer regionsAfterGC, Integer maxRegions) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addRegionCount(regionName, regionsBeforeGC, regionsAfterGC, maxRegions);
    }

    void addRegionSizes(Long sequenceId, String regionName, Integer size, Integer wasted) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addRegionSizes(regionName, size, wasted);
    }

    void addLiveHumongous(Long sequenceId, Long size) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addLiveHumongous(size);
    }

    void addDeadHumongous(Long sequenceId, Long size) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addDeadHumongous(size);
    }

    void addAgeWithSize(Long sequenceId, int age, long size) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addAgeWithSize(age, size);
    }

    void toSpaceExhausted(Long sequenceId) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.toSpaceExhausted();
    }

    void newConcurrentCycle(Long sequenceId, BigDecimal time) {
        concurrentCycleEntries.add(new GCLogConcurrentCycleEntry(sequenceId, time));
    }

    void addSurvivorStats(Long sequenceId, long desiredSize, long newThreshold, long maxThreshold) {
        GCLogCycleEntry gcLogCycleEntry = unprocessedCycles.get(sequenceId);
        if (gcLogCycleEntry == null) {
            return;
        }
        gcLogCycleEntry.addSurvivorStats(desiredSize, newThreshold, maxThreshold);
    }

    void parsingCompleted() {
        if (stats != null) {
            return;
        }

        stats = GCStatsCreator.createStats(this);
    }
}
