package com.pascalhain.runconquer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pascalhain.runconquer.data.mock.MockDataProvider;
import com.pascalhain.runconquer.data.model.OwnerType;
import com.pascalhain.runconquer.data.model.RoutePoint;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Territory;
import com.pascalhain.runconquer.data.repository.RunConquerRepository;
import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;
import com.pascalhain.runconquer.ui.map.RouteAnalyzer;
import com.pascalhain.runconquer.ui.map.TerritoryPolygon;

import org.osmdroid.util.GeoPoint;

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

        List<Run> runs = new ArrayList<>();
        List<Run> recent = repository.getRecentRuns();
        if (recent != null) runs.addAll(recent);
        List<Run> dummyEnemyRuns = MockDataProvider.getRecentRuns();
        if (dummyEnemyRuns != null) runs.addAll(dummyEnemyRuns);

        List<TerritoryPolygon> polygons = new ArrayList<>();
        for (Run run : runs) {
            if (run == null) continue;
            List<RoutePoint> route = run.getRoutePoints();
            if (!RouteAnalyzer.canCreateTerritory(route)) continue;
            OwnerType ownerType = "local_user".equals(run.getUserId()) ? OwnerType.SELF : OwnerType.ENEMY;
            int strength = Math.max(10, Math.min(100, (int) Math.round(run.getDistanceKm() * 12)));
            TerritoryPolygon incoming = new TerritoryPolygon(
                    run.getId(), ownerType, strength, RouteAnalyzer.toPolygon(route));
            applyOverlapRule(polygons, incoming);
        }
        territoryPolygons.setValue(polygons);
    }

    private void applyOverlapRule(List<TerritoryPolygon> polygons, TerritoryPolygon incoming) {
        for (int i = 0; i < polygons.size(); i++) {
            TerritoryPolygon current = polygons.get(i);
            if (!isOverlapping(current, incoming)) continue;

            if (current.getOwnerType() == incoming.getOwnerType()) {
                int boosted = Math.min(100, current.getStrength() + 8);
                polygons.set(i, new TerritoryPolygon(current.getId(), current.getOwnerType(), boosted, current.getPoints()));
                return;
            }

            int reduced = current.getStrength() - Math.max(10, incoming.getStrength() / 4);
            if (reduced <= 0) {
                polygons.set(i, incoming);
            } else {
                polygons.set(i, new TerritoryPolygon(current.getId(), current.getOwnerType(), reduced, current.getPoints()));
            }
            return;
        }
        polygons.add(incoming);
    }

    private boolean isOverlapping(TerritoryPolygon a, TerritoryPolygon b) {
        if (a.getPoints().isEmpty() || b.getPoints().isEmpty()) return false;
        double[] aa = bounds(a.getPoints());
        double[] bb = bounds(b.getPoints());
        return aa[0] <= bb[2] && aa[2] >= bb[0] && aa[1] <= bb[3] && aa[3] >= bb[1];
    }

    private double[] bounds(List<GeoPoint> points) {
        double minLat = Double.MAX_VALUE, minLng = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE, maxLng = -Double.MAX_VALUE;
        for (GeoPoint p : points) {
            minLat = Math.min(minLat, p.getLatitude());
            minLng = Math.min(minLng, p.getLongitude());
            maxLat = Math.max(maxLat, p.getLatitude());
            maxLng = Math.max(maxLng, p.getLongitude());
        }
        return new double[]{minLat, minLng, maxLat, maxLng};
    }
}
