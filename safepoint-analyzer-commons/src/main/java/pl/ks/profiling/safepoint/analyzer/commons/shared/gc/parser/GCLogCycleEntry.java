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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class GCLogCycleEntry {
    private long sequenceId;
    private BigDecimal timeStamp;
    private String phase;
    private String aggregatedPhase;

    private int heapBeforeGC;
    private int heapAfterGC;
    private int heapSize;
    private BigDecimal time;

    private Map<String, BigDecimal> subPhasesTime = new LinkedHashMap<>();

    private Map<String, Integer> regionsAfterGC = new HashMap<>();
    private Map<String, Integer> regionsBeforeGC = new HashMap<>();
    private Map<String, Integer> regionsMax = new HashMap<>();

    private Map<String, Integer> regionsSizeAfterGC = new HashMap<>();
    private Map<String, Integer> regionsWastedAfterGC = new HashMap<>();

    private List<Long> liveHumongousSizes = new ArrayList<>();
    private List<Long> deadHumongousSizes = new ArrayList<>();

    boolean genuineCollection;

    private Map<Integer, Long> bytesInAges = new HashMap<>();
    private int maxAge;

    private long desiredSurvivorSize;
    private long newTenuringThreshold;
    private long maxTenuringThreshold;

    private boolean wasToSpaceExhausted;

    GCLogCycleEntry(Long sequenceId, String phase, BigDecimal timeStamp) {
        this.sequenceId = sequenceId;
        this.phase = phase;
        this.timeStamp = timeStamp;

        if (phase.contains("Pause Young")) {
            if (phase.contains("(Mixed)")) {
                aggregatedPhase = "Mixed collection";
                genuineCollection = true;
            } else if (phase.contains("(Allocation Failure)")) {
                aggregatedPhase = "Young collection";
                genuineCollection = true;
            } else if (phase.contains("(Normal)")) {
                aggregatedPhase = "Young collection";
                genuineCollection = true;
            } else {
                aggregatedPhase = "Young collection - piggybacks";
            }
        } else if (phase.contains("Pause Full")) {
            aggregatedPhase = "Full collection";
            genuineCollection = true;
        } else {
            aggregatedPhase = phase;
        }
    }

    void addSubPhaseTime(String phase, BigDecimal time) {
        subPhasesTime.put(phase, time);
    }

    void addSizesAndTime(int heapBeforeGC, int heapAfterGC, int heapSize, BigDecimal time) {
        this.heapBeforeGC = heapBeforeGC;
        this.heapAfterGC = heapAfterGC;
        this.heapSize = heapSize;
        this.time = time;
    }

    void addRegionCount(String regionName, Integer beforeGC, Integer afterGC, Integer maxRegions) {
        regionsBeforeGC.put(regionName, beforeGC);
        regionsAfterGC.put(regionName, afterGC);
        regionsMax.put(regionName, maxRegions);
    }

    void addRegionSizes(String regionName, Integer size, Integer wasted) {
        regionsSizeAfterGC.put(regionName, size);
        regionsWastedAfterGC.put(regionName, wasted);
    }

    void addLiveHumongous(Long size) {
        liveHumongousSizes.add(size);
    }

    void addDeadHumongous(Long size) {
        deadHumongousSizes.add(size);
    }

    void addAgeWithSize(int age, long size) {
        bytesInAges.put(age, size);
        maxAge = Math.max(age, maxAge);
    }

    void toSpaceExhausted() {
        wasToSpaceExhausted = true;
    }

    void addSurvivorStats(long desiredSize, long newThreshold, long maxThreshold) {
        this.desiredSurvivorSize = desiredSize;;
        this.newTenuringThreshold = newThreshold;
        this.maxTenuringThreshold = maxThreshold;
    }
}
