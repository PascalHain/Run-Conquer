package com.pascalhain.runconquer.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.data.source.MockRunConquerDataSource;
import com.pascalhain.runconquer.data.source.RunDataSource;
import com.pascalhain.runconquer.data.source.TerritoryDataSource;
import com.pascalhain.runconquer.data.source.UserDataSource;
import com.pascalhain.runconquer.data.local.AppDatabase;
import com.pascalhain.runconquer.data.source.LocalRunDataSource;

import java.util.List;

public class RunConquerRepositoryImpl implements RunConquerRepository {

    private static RunConquerRepositoryImpl instance;
    private static Context appContext;

    private final UserDataSource userDataSource;
    private final RunDataSource runDataSource;
    private final TerritoryDataSource territoryDataSource;
    private final MutableLiveData<List<Run>> recentRunsLive = new MutableLiveData<>();

    private RunConquerRepositoryImpl(Context context) {
        MockRunConquerDataSource dataSource = new MockRunConquerDataSource();
        this.userDataSource = dataSource;
        this.territoryDataSource = dataSource;
        AppDatabase database = AppDatabase.getInstance(context);
        this.runDataSource = new LocalRunDataSource(database.runDao(), database.routePointDao());
        recentRunsLive.setValue(runDataSource.getRecentRuns());
    }

    public static synchronized void initialize(Context context) {
        if (context != null) {
            appContext = context.getApplicationContext();
        }
    }

    public static synchronized RunConquerRepositoryImpl getInstance() {
        if (instance == null) {
            if (appContext == null) {
                throw new IllegalStateException("RunConquerRepositoryImpl.initialize(context) must be called first.");
            }
            instance = new RunConquerRepositoryImpl(appContext);
        }
        return instance;
    }

    @Override
    public User getCurrentUser() {
        return userDataSource.getCurrentUser();
    }

    @Override
    public List<Run> getRecentRuns() {
        return runDataSource.getRecentRuns();
    }

    @Override
    public LiveData<List<Run>> getRecentRunsLive() {
        return recentRunsLive;
    }

    @Override
    public void refreshRecentRuns() {
        recentRunsLive.setValue(runDataSource.getRecentRuns());
    }

    @Override
    public List<Territory> getTerritories() {
        return territoryDataSource.getTerritories();
    }

    @Override
    public Team getCurrentTeam() {
        return userDataSource.getCurrentTeam();
    }

    @Override
    public Run getLastRun() {
        return runDataSource.getLastRun();
    }

    @Override
    public void saveRun(Run run) {
        runDataSource.saveRun(run);
        refreshRecentRuns();
    }

    @Override
    public Run createMockCompletedRun() {
        return runDataSource.createMockCompletedRun();
    }

    @Override
    public void claimTerritoryForLastRun(Run run) {
        // TODO: Add real territory calculation based on GPS route.
        territoryDataSource.claimTerritoryForRun(run);
    }
}
