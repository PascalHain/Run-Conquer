package com.pascalhain.runconquer.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.SeasonStats;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.RunTileAdapter;
import com.pascalhain.runconquer.viewmodel.ProfileViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private RunTileAdapter runTileAdapter;
    private User currentUser;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final WeekFields weekFields = WeekFields.ISO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        TextView textProfileName = view.findViewById(R.id.textProfileName);
        TextView textTotalDistance = view.findViewById(R.id.textTotalDistance);
        TextView textProfileLevel = view.findViewById(R.id.textProfileLevel);
        ProgressBar progressProfileLevel = view.findViewById(R.id.progressProfileLevel);

        TextView textStatsRuns = view.findViewById(R.id.textStatsRuns);
        TextView textStatsBestDistance = view.findViewById(R.id.textStatsBestDistance);
        TextView textStatsAvgPace = view.findViewById(R.id.textStatsAvgPace);

        TextView textWeeklyGoal = view.findViewById(R.id.textWeeklyGoal);
        ProgressBar progressWeeklyGoal = view.findViewById(R.id.progressWeeklyGoal);

        RecyclerView recyclerProfileRuns = view.findViewById(R.id.recyclerProfileRuns);
        runTileAdapter = new RunTileAdapter();
        runTileAdapter.setOnRunClickListener(run -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showRunDetail(run);
            }
        });
        recyclerProfileRuns.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerProfileRuns.setAdapter(runTileAdapter);
        recyclerProfileRuns.setNestedScrollingEnabled(false);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
             bindUser(user, textProfileName, textProfileLevel, progressProfileLevel,
                    textWeeklyGoal, progressWeeklyGoal);
        });

        viewModel.getSeasonStats().observe(getViewLifecycleOwner(), stats ->
                bindSeasonStats(stats, textTotalDistance, textStatsRuns,
                        textStatsBestDistance, textStatsAvgPace));

        viewModel.getRecentRuns().observe(getViewLifecycleOwner(), runs -> {
            runTileAdapter.submitRuns(runs);
            bindWeeklyProgress(runs, textWeeklyGoal, progressWeeklyGoal);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshRecentRuns();
        }
    }

    private void bindUser(User user, TextView textProfileName, TextView textProfileLevel,
                          ProgressBar progressProfileLevel, TextView textWeeklyGoal,
                          ProgressBar progressWeeklyGoal) {
        if (user == null) {
            return;
        }
        textProfileName.setText(user.getName());
        textProfileLevel.setText(String.format(Locale.US, "Level %d", user.getLevel()));
        progressProfileLevel.setMax(100);
        progressProfileLevel.setProgress(user.getXp() % 100);

        textWeeklyGoal.setText(String.format(Locale.US, "0.0 / %.0f km", user.getWeeklyGoalKm()));
        progressWeeklyGoal.setMax((int) user.getWeeklyGoalKm());
        progressWeeklyGoal.setProgress(0);
    }

    private void bindSeasonStats(SeasonStats stats, TextView textTotalDistance,
                                 TextView textStatsRuns, TextView textStatsBestDistance,
                                 TextView textStatsAvgPace) {
        if (stats == null) {
            return;
        }
        textTotalDistance.setText(String.format(Locale.US, "Gesamt: %.1f km",
                stats.getTotalDistanceKm()));
        textStatsRuns.setText(String.format(Locale.US, "Laeufe: %d", stats.getTotalRuns()));
        textStatsBestDistance.setText(String.format(Locale.US, "Beste Distanz: %.1f km",
                stats.getBestDistanceKm()));
        textStatsAvgPace.setText(String.format(Locale.US, "Durchschnittl. Pace: %s",
                stats.getAveragePace()));
    }

    private void bindWeeklyProgress(java.util.List<com.pascalhain.runconquer.data.model.Run> runs,
                                    TextView textWeeklyGoal, ProgressBar progressWeeklyGoal) {
        if (currentUser == null) {
            return;
        }
        double weeklyDistance = 0.0;
        LocalDate today = LocalDate.now();
        int currentWeek = today.get(weekFields.weekOfWeekBasedYear());
        int currentYear = today.getYear();
        if (runs != null) {
            for (com.pascalhain.runconquer.data.model.Run run : runs) {
                if (run == null || run.getDate() == null) {
                    continue;
                }
                try {
                    LocalDate runDate = LocalDate.parse(run.getDate(), dateFormatter);
                    int runWeek = runDate.get(weekFields.weekOfWeekBasedYear());
                    if (runDate.getYear() == currentYear && runWeek == currentWeek) {
                        weeklyDistance += run.getDistanceKm();
                    }
                } catch (Exception ignored) {
                    continue;
                }
            }
        }
        textWeeklyGoal.setText(String.format(Locale.US, "%.1f / %.0f km", weeklyDistance,
                currentUser.getWeeklyGoalKm()));
        progressWeeklyGoal.setMax((int) currentUser.getWeeklyGoalKm());
        progressWeeklyGoal.setProgress((int) weeklyDistance);
    }
}
