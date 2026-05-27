package com.pascalhain.runconquer.data.repository;

import androidx.lifecycle.LiveData;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.model.User;

import java.util.List;

public interface RunConquerRepository {
    User getCurrentUser();
    List<Run> getRecentRuns();
    LiveData<List<Run>> getRecentRunsLive();
    void refreshRecentRuns();
    List<Territory> getTerritories();
    Team getCurrentTeam();
    Run getLastRun();
    void saveRun(Run run);
    Run createMockCompletedRun();
    void claimTerritoryForLastRun(Run run);
}
