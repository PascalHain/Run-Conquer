package com.pascalhain.runconquer.ui.summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.osmdroid.views.MapView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;
import com.pascalhain.runconquer.viewmodel.RunSummaryViewModel;

import java.util.Locale;

public class RunSummaryFragment extends Fragment {

    private RunSummaryViewModel viewModel;
    private Run lastRun;
    private MapView mapSummary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RunSummaryViewModel.class);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisible(false);
        }

        TextView textDistance = view.findViewById(R.id.textSummaryDistance);
        TextView textDuration = view.findViewById(R.id.textSummaryDuration);
        TextView textPace = view.findViewById(R.id.textSummaryPace);
        TextView textCalories = view.findViewById(R.id.textSummaryCalories);
        TextView textTerritory = view.findViewById(R.id.textSummaryTerritory);
        TextView textXp = view.findViewById(R.id.textSummaryXp);
        TextView textLocation = view.findViewById(R.id.textSummaryLocation);
        mapSummary = view.findViewById(R.id.mapSummary);
        OsmMapHelper.configure(requireContext(), mapSummary);
        OsmMapHelper.updateLocation(mapSummary, null, null, true);
        Button buttonToMap = view.findViewById(R.id.buttonToMap);
        Button buttonToHome = view.findViewById(R.id.buttonToHome);
        Button buttonShare = view.findViewById(R.id.buttonShareRun);
        Button buttonSave = view.findViewById(R.id.buttonSaveRun);

        viewModel.getLastRun().observe(getViewLifecycleOwner(), run -> {
            lastRun = run;
            bindRun(run, textDistance, textDuration, textPace, textCalories, textTerritory, textXp,
                    textLocation, mapSummary);
        });

        buttonShare.setOnClickListener(v -> shareRun());
        buttonSave.setOnClickListener(v -> showSavedMessage());

        buttonToMap.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showMap();
            }
        });
        buttonToHome.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHome();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (mapSummary != null) {
            mapSummary.onDetach();
            mapSummary = null;
        }
        super.onDestroyView();
    }

    private void bindRun(Run run, TextView textDistance, TextView textDuration,
                         TextView textPace, TextView textCalories, TextView textTerritory,
                         TextView textXp, TextView textLocation, MapView mapSummary) {
        if (run == null) {
            return;
        }
        textDistance.setText(String.format(Locale.US, "%.2f km", run.getDistanceKm()));
        textDuration.setText(run.getDuration());
        textPace.setText(run.getPace());
        textCalories.setText(String.format(Locale.US, "%d kcal", run.getCalories()));
        textTerritory.setText(viewModel.getTerritoryName(run.getConqueredTerritoryId()));
        textXp.setText(String.format(Locale.US, "+%d XP", run.getEarnedXp()));
        textLocation.setText(run.getLocationName() == null ? "" : run.getLocationName());
        if (run.getRoutePoints() != null && !run.getRoutePoints().isEmpty()) {
            OsmMapHelper.updateRoute(mapSummary, run.getRoutePoints(), true);
        } else {
            OsmMapHelper.updateLocation(mapSummary, run.getLatitude(), run.getLongitude(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapSummary != null) {
            mapSummary.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapSummary != null) {
            mapSummary.onPause();
        }
        super.onPause();
    }

    private void shareRun() {
        if (lastRun == null) {
            return;
        }
        String title = lastRun.getLocationName() == null ? "Mein Run" : lastRun.getLocationName();
        String date = lastRun.getDate() == null ? "" : lastRun.getDate();
        String shareText = String.format(Locale.US,
                "%s\n%s\nDistanz: %.2f km\nZeit: %s\nPace: %s\nKalorien: %d kcal",
                title, date, lastRun.getDistanceKm(), lastRun.getDuration(), lastRun.getPace(),
                lastRun.getCalories());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Run Zusammenfassung");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "Teilen"));
    }

    private void showSavedMessage() {
        if (lastRun == null) {
            return;
        }
        viewModel.saveRun(lastRun);
        if (getContext() == null) {
            return;
        }
        Toast.makeText(getContext(), "Run gespeichert", Toast.LENGTH_SHORT).show();
    }
}
