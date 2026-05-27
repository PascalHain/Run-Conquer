package com.pascalhain.runconquer.data.source;

import com.pascalhain.runconquer.data.model.Run;

import java.util.List;

public interface RunDataSource {
    List<Run> getRecentRuns();
    Run getLastRun();
    void saveRun(Run run);
    Run createMockCompletedRun();
}
