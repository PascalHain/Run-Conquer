package com.pascalhain.runconquer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.ui.home.HomeFragment;
import com.pascalhain.runconquer.ui.map.MapFragment;
import com.pascalhain.runconquer.ui.profile.ProfileFragment;
import com.pascalhain.runconquer.ui.team.TeamFragment;
import com.pascalhain.runconquer.ui.tracking.TrackingFragment;
import com.pascalhain.runconquer.viewmodel.TrackingViewModel;

import com.pascalhain.runconquer.ui.tracking.StartRunFragment;
import com.pascalhain.runconquer.ui.summary.RunDetailFragment;
import com.pascalhain.runconquer.ui.summary.RunSummaryFragment;

public class MainActivity extends AppCompatActivity {

    private View navHome;
    private View navMap;
    private View navActivity;
    private View navTeam;
    private View navProfile;
    private View bottomNav;

    private TextView iconHome;
    private TextView labelHome;
    private TextView iconMap;
    private TextView labelMap;
    private TextView iconTeam;
    private TextView labelTeam;
    private TextView iconProfile;
    private TextView labelProfile;
    private TrackingViewModel trackingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHome = findViewById(R.id.nav_home);
        navMap = findViewById(R.id.nav_map);
        navActivity = findViewById(R.id.nav_activity);
        navTeam = findViewById(R.id.nav_team);
        navProfile = findViewById(R.id.nav_profile);
        bottomNav = findViewById(R.id.bottom_nav);

        iconHome = findViewById(R.id.icon_home);
        labelHome = findViewById(R.id.label_home);
        iconMap = findViewById(R.id.icon_map);
        labelMap = findViewById(R.id.label_map);
        iconTeam = findViewById(R.id.icon_team);
        labelTeam = findViewById(R.id.label_team);
        iconProfile = findViewById(R.id.icon_profile);
        labelProfile = findViewById(R.id.label_profile);

        trackingViewModel = new ViewModelProvider(this).get(TrackingViewModel.class);
        trackingViewModel.isRunning().observe(this, running -> updateBottomNavForRun());
        trackingViewModel.isPaused().observe(this, paused -> updateBottomNavForRun());

        navHome.setOnClickListener(v -> {
            showHome();
            setActiveTab(Tab.HOME);
        });
        navMap.setOnClickListener(v -> {
            showMap();
            setActiveTab(Tab.MAP);
        });
        navActivity.setOnClickListener(v -> {
            showStartRun();
            setActiveTab(Tab.ACTIVITY);
        });
        navTeam.setOnClickListener(v -> {
            showTeam();
            setActiveTab(Tab.TEAM);
        });
        navProfile.setOnClickListener(v -> {
            showProfile();
            setActiveTab(Tab.PROFILE);
        });

        if (savedInstanceState == null) {
            showHome();
            setActiveTab(Tab.HOME);
        }
    }

    private void updateBottomNavForRun() {
        boolean isRunning = Boolean.TRUE.equals(trackingViewModel.isRunning().getValue());
        boolean isPaused = Boolean.TRUE.equals(trackingViewModel.isPaused().getValue());
        if (isRunning || isPaused) {
            setBottomNavVisible(false);
        }
    }

    public void showHome() {
        setBottomNavVisible(true);
        replaceFragment(new HomeFragment());
    }

    public void showMap() {
        setBottomNavVisible(true);
        replaceFragment(new MapFragment());
    }

    public void showTracking() {
        setBottomNavVisible(false);
        replaceFragment(TrackingFragment.newInstance(false));
    }

    public void showTracking(boolean autoStart) {
        setBottomNavVisible(false);
        replaceFragment(TrackingFragment.newInstance(autoStart));
    }

    public void showStartRun() {
        setBottomNavVisible(false);
        replaceFragment(new StartRunFragment());
    }

    public void showTeam() {
        replaceFragment(new TeamFragment());
    }

    public void showProfile() {
        replaceFragment(new ProfileFragment());
    }

    public void showRunSummary() {
        setBottomNavVisible(false);
        replaceFragment(new RunSummaryFragment());
    }

    public void showRunDetail(com.pascalhain.runconquer.data.model.Run run) {
        replaceFragment(RunDetailFragment.newInstance(run));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setActiveTab(Tab tab) {
        int activeColor = getResources().getColor(R.color.colorPrimary);
        int inactiveColor = getResources().getColor(R.color.white);

        iconHome.setTextColor(tab == Tab.HOME ? activeColor : inactiveColor);
        labelHome.setTextColor(tab == Tab.HOME ? activeColor : inactiveColor);
        iconMap.setTextColor(tab == Tab.MAP ? activeColor : inactiveColor);
        labelMap.setTextColor(tab == Tab.MAP ? activeColor : inactiveColor);
        iconTeam.setTextColor(tab == Tab.TEAM ? activeColor : inactiveColor);
        labelTeam.setTextColor(tab == Tab.TEAM ? activeColor : inactiveColor);
        iconProfile.setTextColor(tab == Tab.PROFILE ? activeColor : inactiveColor);
        labelProfile.setTextColor(tab == Tab.PROFILE ? activeColor : inactiveColor);
    }

    public void setBottomNavVisible(boolean visible) {
        if (bottomNav == null) {
            return;
        }
        bottomNav.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private enum Tab {
        HOME,
        MAP,
        ACTIVITY,
        TEAM,
        PROFILE
    }
}
