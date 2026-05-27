package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<List<Territory>> territories = new MutableLiveData<>(repository.getTerritories());

    public LiveData<List<Territory>> getTerritories() {
        return territories;
    }
}
