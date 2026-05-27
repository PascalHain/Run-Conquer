package com.pascalhain.runconquer.ui.tracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.osmdroid.views.MapView;
import android.widget.TextView;

import com.pascalhain.runconquer.data.model.RoutePoint;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

public class TrackingFragment extends Fragment {

    private static final String ARG_AUTO_START = "arg_auto_start";

    private TrackingViewModel viewModel;
    private Button buttonStart;
    private Button buttonPause;
    private Button buttonStop;
    private MapView mapTracking;

    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;
    private ActivityResultLauncher<String[]> permissionLauncher;

    public static TrackingFragment newInstance(boolean autoStart) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_AUTO_START, autoStart);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TrackingViewModel.class);

        mapTracking = view.findViewById(R.id.mapTracking);
        TextView textTimer = view.findViewById(R.id.textTimer);
        TextView textDistance = view.findViewById(R.id.textTrackingDistance);
        TextView textPace = view.findViewById(R.id.textTrackingPace);
        TextView textTime = view.findViewById(R.id.textTrackingTime);
        TextView textCalories = view.findViewById(R.id.textTrackingCalories);
        buttonStart = view.findViewById(R.id.buttonStartRun);
        buttonPause = view.findViewById(R.id.buttonPauseRun);
        buttonStop = view.findViewById(R.id.buttonStopRun);

        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location == null) {
                    return;
                }
                viewModel.updateLocation(location);
            }
        };
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (hasLocationPermission()) {
                        OsmMapHelper.enableMyLocation(requireContext(), mapTracking, true);
                        startLocationUpdates();
                    }
                });

        OsmMapHelper.configure(requireContext(), mapTracking);
        OsmMapHelper.updateLocation(mapTracking, null, null, true);
        if (hasLocationPermission()) {
            OsmMapHelper.enableMyLocation(requireContext(), mapTracking, true);
        }

        viewModel.isRunning().observe(getViewLifecycleOwner(), running -> updateControls());
        viewModel.isPaused().observe(getViewLifecycleOwner(), paused -> updateControls());
        viewModel.getLastLocation().observe(getViewLifecycleOwner(), location -> {
            if (location == null) {
                return;
            }
            if (!hasRoutePoints()) {
                OsmMapHelper.updateLocation(
                        mapTracking,
                        location.getLatitude(),
                        location.getLongitude(),
                        true
                );
            }
        });
        viewModel.getRoutePoints().observe(getViewLifecycleOwner(), points -> {
            OsmMapHelper.updateRoute(mapTracking, points, true);
            if (points != null && !points.isEmpty() && hasLocationPermission()) {
                OsmMapHelper.enableMyLocation(requireContext(), mapTracking, true);
            }
        });

        viewModel.getTimerText().observe(getViewLifecycleOwner(), textTimer::setText);
        viewModel.getTimerText().observe(getViewLifecycleOwner(), textTime::setText);
        viewModel.getDistanceText().observe(getViewLifecycleOwner(), textDistance::setText);
        viewModel.getPaceText().observe(getViewLifecycleOwner(), textPace::setText);
        viewModel.getCaloriesText().observe(getViewLifecycleOwner(), textCalories::setText);

        buttonStart.setOnClickListener(v -> viewModel.startRun());
        buttonPause.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.isPaused().getValue())) {
                viewModel.resumeRun();
            } else {
                viewModel.pauseRun();
            }
        });
        buttonStop.setOnClickListener(v -> {
            viewModel.completeRun();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showRunSummary();
            }
        });

        updateControls();

        if (shouldAutoStart()) {
            viewModel.startRun();
        }

        requestLocationIfNeeded();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasLocationPermission()) {
            OsmMapHelper.enableMyLocation(requireContext(), mapTracking, true);
            startLocationUpdates();
        }
        if (mapTracking != null) {
            mapTracking.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
        if (mapTracking != null) {
            mapTracking.onPause();
        }
    }

    private void requestLocationIfNeeded() {
        if (!hasLocationPermission()) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            OsmMapHelper.enableMyLocation(requireContext(), mapTracking, true);
            startLocationUpdates();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationUpdates() {
        LocationRequest request = new LocationRequest.Builder(3000L)
                .setMinUpdateIntervalMillis(1500L)
                .setPriority(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY)
                .build();
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                viewModel.updateLocation(location);
            }
        });
        locationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        if (locationClient != null && locationCallback != null) {
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    private boolean hasRoutePoints() {
        if (viewModel == null || viewModel.getRoutePoints().getValue() == null) {
            return false;
        }
        java.util.List<RoutePoint> points = viewModel.getRoutePoints().getValue();
        return points != null && !points.isEmpty();
    }

    private boolean shouldAutoStart() {
        Bundle args = getArguments();
        return args != null && args.getBoolean(ARG_AUTO_START, false);
    }

    private void updateControls() {
        boolean isRunning = Boolean.TRUE.equals(viewModel.isRunning().getValue());
        boolean isPaused = Boolean.TRUE.equals(viewModel.isPaused().getValue());
        boolean inRunMode = isRunning || isPaused;

        buttonStart.setVisibility(inRunMode ? View.GONE : View.VISIBLE);
        buttonPause.setVisibility(inRunMode ? View.VISIBLE : View.GONE);
        buttonStop.setVisibility(inRunMode ? View.VISIBLE : View.GONE);
        buttonPause.setText(isPaused ? "Weiter" : "Pause");

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisible(!inRunMode);
        }
    }

    @Override
    public void onDestroyView() {
        if (mapTracking != null) {
            mapTracking.onDetach();
            mapTracking = null;
        }
        super.onDestroyView();
    }
}
