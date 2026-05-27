package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<User> user = new MutableLiveData<>(repository.getCurrentUser());
    private final LiveData<List<Run>> recentRuns = repository.getRecentRunsLive();
    private final MutableLiveData<Team> team = new MutableLiveData<>(repository.getCurrentTeam());

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Run>> getRecentRuns() {
        return recentRuns;
    }

    public LiveData<Team> getTeam() {
        return team;
    }

    public void refreshRecentRuns() {
        repository.refreshRecentRuns();
    }
}
