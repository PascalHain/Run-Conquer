package com.pascalhain.runconquer.ui.tracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.osmdroid.views.MapView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

public class StartRunFragment extends Fragment {

    private TrackingViewModel trackingViewModel;
    private FusedLocationProviderClient locationClient;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private CancellationTokenSource cancellationTokenSource;
    private MapView mapStartMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_run, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonStartRun = view.findViewById(R.id.buttonStartRun);
        mapStartMap = view.findViewById(R.id.mapStartMap);
        trackingViewModel = new ViewModelProvider(requireActivity()).get(TrackingViewModel.class);
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (hasLocationPermission()) {
                        OsmMapHelper.enableMyLocation(requireContext(), mapStartMap, true);
                        requestLastLocation();
                    }
                });

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisible(false);
        }

        OsmMapHelper.configure(requireContext(), mapStartMap);
        OsmMapHelper.updateLocation(mapStartMap, null, null, true);
        if (hasLocationPermission()) {
            OsmMapHelper.enableMyLocation(requireContext(), mapStartMap, true);
        }

        trackingViewModel.getLastLocation().observe(getViewLifecycleOwner(), location -> {
            if (location == null) {
                return;
            }
            OsmMapHelper.updateLocation(mapStartMap, location.getLatitude(), location.getLongitude(), true);
        });

        buttonStartRun.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showTracking(true);
            }
        });

        if (hasLocationPermission()) {
            requestLastLocation();
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapStartMap != null) {
            mapStartMap.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapStartMap != null) {
            mapStartMap.onPause();
        }
        super.onPause();
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLastLocation() {
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                trackingViewModel.updateLocation(location);
            } else {
                requestCurrentLocation();
            }
        });
    }

    private void requestCurrentLocation() {
        if (cancellationTokenSource != null) {
            cancellationTokenSource.cancel();
        }
        cancellationTokenSource = new CancellationTokenSource();
        locationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        ).addOnSuccessListener(location -> {
            if (location != null) {
                trackingViewModel.updateLocation(location);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (cancellationTokenSource != null) {
            cancellationTokenSource.cancel();
            cancellationTokenSource = null;
        }
        if (mapStartMap != null) {
            mapStartMap.onDetach();
            mapStartMap = null;
        }
        super.onDestroyView();
    }
}
