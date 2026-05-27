package com.pascalhain.runconquer.data.source;

import com.pascalhain.runconquer.data.mock.MockDataProvider;
import com.pascalhain.runconquer.data.model.OwnerType;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class MockRunConquerDataSource implements UserDataSource, RunDataSource, TerritoryDataSource {

    private final User user;
    private final Team team;
    private final List<Run> recentRuns;
    private final List<Territory> territories;
    private Run lastRun;

    public MockRunConquerDataSource() {
        this.user = MockDataProvider.getUser();
        this.team = MockDataProvider.getTeam();
        this.recentRuns = new ArrayList<>(MockDataProvider.getRecentRuns());
        this.territories = new ArrayList<>(MockDataProvider.getTerritories());
        this.lastRun = recentRuns.isEmpty() ? null : recentRuns.get(0);
    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public Team getCurrentTeam() {
        return team;
    }

    @Override
    public List<Run> getRecentRuns() {
        return new ArrayList<>(recentRuns);
    }

    @Override
    public Run getLastRun() {
        return lastRun;
    }

    @Override
    public void saveRun(Run run) {
        if (run == null) {
            return;
        }
        lastRun = run;
        for (int i = 0; i < recentRuns.size(); i++) {
            Run existing = recentRuns.get(i);
            if (existing != null && existing.getId().equals(run.getId())) {
                recentRuns.remove(i);
                break;
            }
        }
        recentRuns.add(0, run);
    }

    @Override
    public Run createMockCompletedRun() {
        // TODO: Replace MockRunConquerDataSource with real Firebase/Room data source.
        return MockDataProvider.getMockCompletedRun();
    }

    @Override
    public List<Territory> getTerritories() {
        return new ArrayList<>(territories);
    }

    @Override
    public void claimTerritoryForRun(Run run) {
        if (run == null) {
            return;
        }
        for (Territory territory : territories) {
            if (territory.getId().equals(run.getConqueredTerritoryId())) {
                territory.setOwnerType(OwnerType.SELF);
                break;
            }
        }
    }
}
