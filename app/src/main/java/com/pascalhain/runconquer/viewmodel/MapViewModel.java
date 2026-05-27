package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.model.OwnerType;
import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;
import com.pascalhain.runconquer.ui.map.RouteAnalyzer;
import com.pascalhain.runconquer.ui.map.TerritoryPolygon;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    private final RunConquerRepository repository = RunConquerRepositoryImpl.getInstance();
    private final MutableLiveData<List<Territory>> territories = new MutableLiveData<>(repository.getTerritories());
    private final MutableLiveData<List<TerritoryPolygon>> territoryPolygons = new MutableLiveData<>(new ArrayList<>());

    public MapViewModel() {
        refreshTerritories();
    }

    public LiveData<List<Territory>> getTerritories() {
        return territories;
    }

    public LiveData<List<TerritoryPolygon>> getTerritoryPolygons() {
        return territoryPolygons;
    }

    public void refreshTerritories() {
        repository.refreshRecentRuns();
        territories.setValue(repository.getTerritories());
        List<Run> runs = repository.getRecentRuns();
        List<TerritoryPolygon> polygons = new ArrayList<>();
        if (runs != null) {
            for (Run run : runs) {
                if (run == null) {
                    continue;
                }
                List<RoutePoint> route = run.getRoutePoints();
                if (!RouteAnalyzer.canCreateTerritory(route)) {
                    continue;
                }
                OwnerType ownerType = "local_user".equals(run.getUserId()) ? OwnerType.SELF : OwnerType.ENEMY;
                int strength = Math.max(10, Math.min(100, (int) Math.round(run.getDistanceKm() * 12)));
                polygons.add(new TerritoryPolygon(
                        run.getId(), ownerType, strength, RouteAnalyzer.toPolygon(route)));
            }
        }
        territoryPolygons.setValue(polygons);
    }
}
