package pl.ks.profiling.safepoint.analyzer.commons.shared.page;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import pl.ks.profiling.gui.commons.Chart;
import pl.ks.profiling.gui.commons.Page;
import pl.ks.profiling.safepoint.analyzer.commons.shared.pareser.gc.GcCycleInfo;
import pl.ks.profiling.safepoint.analyzer.commons.shared.pareser.safepoint.JvmLogFile;

public class GCHeapBefore implements PageCreator{
    @Override
    public Page create(JvmLogFile jvmLogFile, DecimalFormat decimalFormat) {
        return Page.builder()
                .menuName("Heap before GC")
                .fullName("Heap before GC")
                .info("These charts presents heap size before Garbage Collection. There are displayed only young, mixed and full collections. " +
                        "There are not displayed any piggybacked and concurrent collections.")
                .icon(Page.Icon.CHART)
                .pageContents(List.of(
                        Chart.builder()
                                .chartType(Chart.ChartType.POINTS_OR_LINE)
                                .seriesTypes(new Chart.SeriesType[] {Chart.SeriesType.POINTS, Chart.SeriesType.LINE})
                                .title("Heap before GC")
                                .data(getHeapBeforeGCSizeChart(jvmLogFile))
                                .build()
                ))
                .build();
    }


    private static Object[][] getHeapBeforeGCSizeChart(JvmLogFile jvmLogFile) {
        List<GcCycleInfo> cyclesToShow = jvmLogFile.getGcLogFile().getGcCycleInfos()
                .stream()
                .filter(GcCycleInfo::isGenuineCollection)
                .collect(Collectors.toList());
        Object[][] stats = new Object[cyclesToShow.size() + 1][3];
        stats[0][0] = "Cycle";
        stats[0][1] = "Heap before GC";
        stats[0][2] = "Heap size";
        int i = 1;

        for (GcCycleInfo gcCycleInfo : cyclesToShow) {
            stats[i][0] = gcCycleInfo.getTimeStamp();
            stats[i][1] = gcCycleInfo.getHeapBeforeGC();
            stats[i][2] = gcCycleInfo.getHeapSize();
            i++;
        }

        return stats;
    }
}