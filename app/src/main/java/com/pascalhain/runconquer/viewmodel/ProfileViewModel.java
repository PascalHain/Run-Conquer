package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.SeasonStats;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<User> user = new MutableLiveData<>(repository.getCurrentUser());
    private final LiveData<List<Run>> recentRuns = repository.getRecentRunsLive();
    private final MediatorLiveData<SeasonStats> seasonStats = new MediatorLiveData<>();

    public ProfileViewModel() {
        seasonStats.addSource(recentRuns, runs -> seasonStats.setValue(SeasonStats.fromRuns(runs)));
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Run>> getRecentRuns() {
        return recentRuns;
    }

    public LiveData<SeasonStats> getSeasonStats() {
        return seasonStats;
    }

    public void refreshRecentRuns() {
        repository.refreshRecentRuns();
    }
}
