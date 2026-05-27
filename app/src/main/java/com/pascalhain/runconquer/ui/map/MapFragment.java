package com.pascalhain.runconquer.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.OwnerType;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;
import com.pascalhain.runconquer.viewmodel.MapViewModel;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.List;

public class MapFragment extends Fragment {

    private TrackingViewModel trackingViewModel;
    private MapViewModel mapViewModel;
    private MapView mapView;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapTerritory);
        OsmMapHelper.configure(requireContext(), mapView);
        OsmMapHelper.updateLocation(mapView, null, null, true);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (hasLocationPermission()) {
                        OsmMapHelper.enableMyLocation(requireContext(), mapView, true);
                    }
                });
        if (hasLocationPermission()) {
            OsmMapHelper.enableMyLocation(requireContext(), mapView, true);
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getTerritoryPolygons().observe(getViewLifecycleOwner(), this::renderTerritories);
        mapViewModel.refreshTerritories();
    }

    private void renderTerritories(List<TerritoryPolygon> polygons) {
        if (mapView == null || polygons == null) {
            return;
        }
        mapView.getOverlays().removeIf(overlay -> overlay instanceof Polygon);
        for (TerritoryPolygon polygonData : polygons) {
            if (polygonData.getPoints() == null || polygonData.getPoints().size() < 3) {
                continue;
            }
            OsmMapHelper.updateLocation(mapView, location.getLatitude(), location.getLongitude(), true);
        });

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getTerritoryPolygons().observe(getViewLifecycleOwner(), this::renderTerritories);
        mapViewModel.refreshTerritories();
    }

    private void renderTerritories(List<TerritoryPolygon> polygons) {
        if (mapView == null || polygons == null) {
            return;
        }
        for (TerritoryPolygon polygonData : polygons) {
            if (polygonData.getPoints() == null || polygonData.getPoints().size() < 3) {
                continue;
            }
            Polygon polygon = new Polygon();
            polygon.setPoints(polygonData.getPoints());
            polygon.setStrokeWidth(4f);
            polygon.setStrokeColor(getStrokeColor(polygonData.getOwnerType()));
            polygon.setFillColor(getFillColor(polygonData.getOwnerType(), polygonData.getStrength()));
            mapView.getOverlays().add(polygon);
        }
        mapView.invalidate();
    }

    private int getStrokeColor(OwnerType ownerType) {
        if (ownerType == OwnerType.SELF) {
            return Color.rgb(255, 59, 48);
        }
        if (ownerType == OwnerType.ENEMY) {
            return Color.rgb(0, 122, 255);
        }
        return Color.rgb(142, 142, 147);
    }

    private int getFillColor(OwnerType ownerType, int strength) {
        int alpha = Math.max(40, Math.min(140, strength + 20));
        if (ownerType == OwnerType.SELF) {
            return Color.argb(alpha, 255, 59, 48);
        }
        if (ownerType == OwnerType.ENEMY) {
            return Color.argb(alpha, 0, 122, 255);
        }
        return Color.argb(alpha, 142, 142, 147);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mapView != null) {
            mapView.onDetach();
            mapView = null;
        }
        super.onDestroyView();
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
}
