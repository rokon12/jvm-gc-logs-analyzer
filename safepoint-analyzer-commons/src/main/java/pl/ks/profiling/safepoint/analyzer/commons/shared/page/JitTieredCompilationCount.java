package pl.ks.profiling.safepoint.analyzer.commons.shared.page;

import java.text.DecimalFormat;
import java.util.List;
import pl.ks.profiling.gui.commons.Chart;
import pl.ks.profiling.gui.commons.Page;
import pl.ks.profiling.safepoint.analyzer.commons.shared.parser.JvmLogFile;
import pl.ks.profiling.safepoint.analyzer.commons.shared.parser.jit.CompilationStatus;

public class JitTieredCompilationCount implements PageCreator {
    @Override
    public Page create(JvmLogFile jvmLogFile, DecimalFormat decimalFormat) {
        return Page.builder()
                .menuName("JIT tiered compilation count")
                .fullName("JIT tiered compilation count")
                .info("Following chart current compilation count in each tear.")
                .icon(Page.Icon.CHART)
                .pageContents(
                        List.of(
                                Chart.builder()
                                        .chartType(Chart.ChartType.LINE)
                                        .title("Current tear count")
                                        .data(getCurrentCountChart(jvmLogFile.getJitLogFile().getCompilationStatuses()))
                                        .build(),
                                Chart.builder()
                                        .chartType(Chart.ChartType.LINE)
                                        .title("Current tear 2 count")
                                        .data(getTierCountChart(jvmLogFile.getJitLogFile().getCompilationStatuses(), 2))
                                        .build(),
                                Chart.builder()
                                        .chartType(Chart.ChartType.LINE)
                                        .title("Current tear 4 count")
                                        .data(getTierCountChart(jvmLogFile.getJitLogFile().getCompilationStatuses(), 4))
                                        .build()
                        )
                )
                .build();
    }

    private static Object[][] getCurrentCountChart(List<CompilationStatus> compilationStatuses) {
        Object[][] stats = new Object[compilationStatuses.size() + 1][5];
        stats[0][0] = "Time";
        stats[0][1] = "Tear 1";
        stats[0][2] = "Tear 2";
        stats[0][3] = "Tear 3";
        stats[0][4] = "Tear 4";
        int i = 1;
        for (CompilationStatus status : compilationStatuses) {
            stats[i][0] = status.getTimeStamp();
            stats[i][1] = status.getTier1CurrentCount();
            stats[i][2] = status.getTier2CurrentCount();
            stats[i][3] = status.getTier3CurrentCount();
            stats[i][4] = status.getTier4CurrentCount();
            i++;
        }
        return stats;
    }

    private static Object[][] getTierCountChart(List<CompilationStatus> compilationStatuses, int tier) {
        Object[][] stats = new Object[compilationStatuses.size() + 1][2];
        stats[0][0] = "Time";
        stats[0][1] = "Tear " + tier;
        int i = 1;
        for (CompilationStatus status : compilationStatuses) {
            stats[i][0] = status.getTimeStamp();
            switch (tier) {
                case 1:
                    stats[i][1] = status.getTier1CurrentCount();
                    break;
                case 2:
                    stats[i][1] = status.getTier2CurrentCount();
                    break;
                case 3:
                    stats[i][1] = status.getTier3CurrentCount();
                    break;
                case 4:
                    stats[i][1] = status.getTier4CurrentCount();
                    break;
            }
            i++;
        }
        return stats;
    }

}
