package com.pascalhain.runconquer.ui.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.Team;
import com.pascalhain.runconquer.viewmodel.TeamViewModel;

import java.util.Locale;

public class TeamFragment extends Fragment {

    private TeamViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TeamViewModel.class);

        TextView textTeamName = view.findViewById(R.id.textTeamName);
        TextView textTeamMembers = view.findViewById(R.id.textTeamMembers);
        TextView textTeamDistance = view.findViewById(R.id.textTeamDistance);

        viewModel.getTeam().observe(getViewLifecycleOwner(), team ->
                bindTeam(team, textTeamName, textTeamMembers, textTeamDistance));

        // TODO: Add real team synchronization.
    }

    private void bindTeam(Team team, TextView textTeamName, TextView textTeamMembers,
                          TextView textTeamDistance) {
        if (team == null) {
            return;
        }
        textTeamName.setText(team.getName());
        textTeamMembers.setText(String.format(Locale.US, "%d Mitglieder", team.getMemberCount()));
        textTeamDistance.setText(String.format(Locale.US, "%.1f km diese Woche", team.getWeeklyDistanceKm()));
    }
}
