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
package pl.ks.profiling.safepoint.analyzer.commons.shared.thread.page;

import java.text.DecimalFormat;
import java.util.List;
import pl.ks.profiling.gui.commons.Chart;
import pl.ks.profiling.gui.commons.Page;
import pl.ks.profiling.safepoint.analyzer.commons.shared.JvmLogFile;
import pl.ks.profiling.safepoint.analyzer.commons.shared.PageCreator;
import pl.ks.profiling.safepoint.analyzer.commons.shared.thread.parser.ThreadsStatus;

public class ThreadCount implements PageCreator {
    @Override
    public Page create(JvmLogFile jvmLogFile, DecimalFormat decimalFormat) {
        return Page.builder()
                .menuName("Thread count/creation")
                .fullName("Thread count/creation")
                .info("Following chart current thread count and creation.")
                .icon(Page.Icon.CHART)
                .pageContents(
                        List.of(
                                Chart.builder()
                                        .chartType(Chart.ChartType.POINTS)
                                        .title("Current count")
                                        .data(getCurrentCountChart(jvmLogFile.getThreadLogFile().getThreadsStatuses()))
                                        .build(),
                                Chart.builder()
                                        .chartType(Chart.ChartType.LINE)
                                        .title("Created")
                                        .data(getCreatedChart(jvmLogFile.getThreadLogFile().getThreadsStatuses()))
                                        .build()
                        )
                )
                .build();
    }

    private static Object[][] getCurrentCountChart(List<ThreadsStatus> threadsStatuses) {
        Object[][] stats = new Object[threadsStatuses.size() + 1][2];
        stats[0][0] = "Time";
        stats[0][1] = "Count";
        int i = 1;
        for (ThreadsStatus status : threadsStatuses) {
            stats[i][0] = status.getTimeStamp();
            stats[i][1] = status.getCurrentCount();
            i++;
        }
        return stats;
    }

    private static Object[][] getCreatedChart(List<ThreadsStatus> threadsStatuses) {
        Object[][] stats = new Object[threadsStatuses.size() + 1][2];
        stats[0][0] = "Time";
        stats[0][1] = "Created";
        int i = 1;
        for (ThreadsStatus status : threadsStatuses) {
            stats[i][0] = status.getTimeStamp();
            stats[i][1] = status.getCreatedCount();
            i++;
        }
        return stats;
    }
}
