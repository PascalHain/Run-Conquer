package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

import java.util.List;

public class RunSummaryViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<Run> lastRun = new MutableLiveData<>(repository.getLastRun());

    public LiveData<Run> getLastRun() {
        return lastRun;
    }

    public void saveRun(Run run) {
        if (run == null) {
            return;
        }
        repository.saveRun(run);
    }

    public String getTerritoryName(String territoryId) {
        List<Territory> territories = repository.getTerritories();
        for (Territory territory : territories) {
            if (territory.getId().equals(territoryId)) {
                return territory.getName();
            }
        }
        return "-";
    }
}
