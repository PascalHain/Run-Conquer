package com.pascalhain.runconquer.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

import org.osmdroid.views.MapView;

public class MapFragment extends Fragment {

    private TrackingViewModel trackingViewModel;
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

        trackingViewModel = new ViewModelProvider(requireActivity()).get(TrackingViewModel.class);
        trackingViewModel.getLastLocation().observe(getViewLifecycleOwner(), location -> {
            if (location == null) {
                return;
            }
            OsmMapHelper.updateLocation(mapView, location.getLatitude(), location.getLongitude(), true);
        });
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
