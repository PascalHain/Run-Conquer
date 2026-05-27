package com.pascalhain.runconquer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.data.model.User;
import com.pascalhain.runconquer.data.mock.MockDataProvider;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.RunTileAdapter;
import com.pascalhain.runconquer.viewmodel.HomeViewModel;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private TrackingViewModel trackingViewModel;
    private RunTileAdapter runTileAdapter;
    private RunTileAdapter communityTileAdapter;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        trackingViewModel = new ViewModelProvider(requireActivity()).get(TrackingViewModel.class);

        View activeRunContainer = view.findViewById(R.id.activeRunContainer);
        TextView textActiveDistance = view.findViewById(R.id.textActiveDistance);
        TextView textActiveTime = view.findViewById(R.id.textActiveTime);
        TextView textActivePace = view.findViewById(R.id.textActivePace);
        TextView textActiveCalories = view.findViewById(R.id.textActiveCalories);

        TextView textGreeting = view.findViewById(R.id.textGreeting);
        TextView textTodayDistance = view.findViewById(R.id.textTodayDistance);
        TextView textCalories = view.findViewById(R.id.textCalories);
        TextView textStreak = view.findViewById(R.id.textStreak);
        TextView textLevel = view.findViewById(R.id.textLevel);
        View buttonStartRun = view.findViewById(R.id.buttonStartRun);

        buttonStartRun.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showStartRun();
            }
        });

        RecyclerView recyclerRecentRuns = view.findViewById(R.id.recyclerRecentRuns);
        runTileAdapter = new RunTileAdapter();
        runTileAdapter.setMaxItems(1);
        runTileAdapter.setTitleOverride("Du");
        runTileAdapter.setSubtitleOverride("hast einen Lauf gemacht");
        runTileAdapter.setOnRunClickListener(run -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showRunDetail(run);
            }
        });
        recyclerRecentRuns.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerRecentRuns.setAdapter(runTileAdapter);
        recyclerRecentRuns.setNestedScrollingEnabled(false);

        RecyclerView recyclerCommunityRuns = view.findViewById(R.id.recyclerCommunityRuns);
        communityTileAdapter = new RunTileAdapter();
        communityTileAdapter.setMaxItems(10);
        String[] friendNames = new String[]{
                "Lena", "Jonas", "Mia", "David", "Sophie", "Alex", "Nina", "Tom"
        };
        communityTileAdapter.setTitleProvider((run, position) -> friendNames[position % friendNames.length]);
        communityTileAdapter.setSubtitleOverride("hat einen Lauf gemacht");
        communityTileAdapter.setOnRunClickListener(run -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showRunDetail(run);
            }
        });
        recyclerCommunityRuns.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerCommunityRuns.setAdapter(communityTileAdapter);
        recyclerCommunityRuns.setNestedScrollingEnabled(false);

        TextView textTeamHome = view.findViewById(R.id.textTeamHome);
        TextView textTeamDistanceHome = view.findViewById(R.id.textTeamDistanceHome);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> bindUser(user, textGreeting,
                textStreak, textLevel));

        viewModel.getRecentRuns().observe(getViewLifecycleOwner(), runs -> {
            runTileAdapter.submitRuns(runs);
            communityTileAdapter.submitRuns(buildCommunityRuns(runs));
            bindTodayStats(runs, textTodayDistance, textCalories);
        });

        trackingViewModel.isRunning().observe(getViewLifecycleOwner(), running ->
                updateActiveRunVisibility(activeRunContainer));
        trackingViewModel.isPaused().observe(getViewLifecycleOwner(), paused ->
                updateActiveRunVisibility(activeRunContainer));
        trackingViewModel.getDistanceText().observe(getViewLifecycleOwner(), textActiveDistance::setText);
        trackingViewModel.getTimerText().observe(getViewLifecycleOwner(), textActiveTime::setText);
        trackingViewModel.getPaceText().observe(getViewLifecycleOwner(), textActivePace::setText);
        trackingViewModel.getCaloriesText().observe(getViewLifecycleOwner(), textActiveCalories::setText);

        updateActiveRunVisibility(activeRunContainer);

        viewModel.getTeam().observe(getViewLifecycleOwner(), team ->
                bindTeam(team, textTeamHome, textTeamDistanceHome));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshRecentRuns();
        }
    }

    private void updateActiveRunVisibility(View activeRunContainer) {
        boolean isRunning = Boolean.TRUE.equals(trackingViewModel.isRunning().getValue());
        boolean isPaused = Boolean.TRUE.equals(trackingViewModel.isPaused().getValue());
        activeRunContainer.setVisibility((isRunning || isPaused) ? View.VISIBLE : View.GONE);
    }

    private void bindUser(User user, TextView textGreeting, TextView textStreak, TextView textLevel) {
        if (user == null) {
            return;
        }
        textGreeting.setText(String.format(Locale.US, "Hi %s", user.getName()));
        textStreak.setText(String.format(Locale.US, "%d Tage", user.getCurrentStreakDays()));
        textLevel.setText(String.format(Locale.US, "Level %d", user.getLevel()));
    }

    private void bindTeam(Team team, TextView textTeamHome, TextView textTeamDistanceHome) {
        if (team == null) {
            return;
        }
        textTeamHome.setText(team.getName());
        textTeamDistanceHome.setText(String.format(Locale.US, "%.1f km diese Woche",
                team.getWeeklyDistanceKm()));
    }

    private List<Run> buildCommunityRuns(List<Run> runs) {
        List<Run> communityRuns = new ArrayList<>();
        HashSet<String> seenIds = new HashSet<>();
        List<Run> mockRuns = MockDataProvider.getRecentRuns();
        if (mockRuns != null) {
            for (Run mockRun : mockRuns) {
                if (mockRun == null) {
                    continue;
                }
                String id = mockRun.getId();
                if (id != null && !seenIds.add(id)) {
                    continue;
                }
                communityRuns.add(mockRun);
            }
        }
        if (runs != null && runs.size() > 1) {
            for (int i = 1; i < runs.size(); i++) {
                Run run = runs.get(i);
                if (run == null) {
                    continue;
                }
                String id = run.getId();
                if (id != null && !seenIds.add(id)) {
                    continue;
                }
                communityRuns.add(run);
            }
        }
        if (communityRuns.isEmpty() && runs != null) {
            for (Run run : runs) {
                if (run == null) {
                    continue;
                }
                String id = run.getId();
                if (id != null && !seenIds.add(id)) {
                    continue;
                }
                communityRuns.add(run);
            }
        }
        return communityRuns;
    }

    private void bindTodayStats(List<Run> runs, TextView textTodayDistance, TextView textCalories) {
        double distanceKm = 0.0;
        int calories = 0;
        LocalDate today = LocalDate.now();
        if (runs != null) {
            for (Run run : runs) {
                if (run == null || run.getDate() == null) {
                    continue;
                }
                try {
                    LocalDate runDate = LocalDate.parse(run.getDate(), dateFormatter);
                    if (today.equals(runDate)) {
                        distanceKm += run.getDistanceKm();
                        calories += run.getCalories();
                    }
                } catch (Exception ignored) {
                    continue;
                }
            }
        }
        textTodayDistance.setText(String.format(Locale.US, "%.1f km", distanceKm));
        textCalories.setText(String.format(Locale.US, "%d kcal", calories));
    }
}
