package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

public class TeamViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<Team> team = new MutableLiveData<>(repository.getCurrentTeam());

    public LiveData<Team> getTeam() {
        return team;
    }
}
