package com.pascalhain.runconquer.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.osmdroid.views.MapView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.Run;
import com.pascalhain.runconquer.ui.MainActivity;
import com.pascalhain.runconquer.ui.common.OsmMapHelper;

import java.util.Locale;

public class RunDetailFragment extends Fragment {
    private MapView mapDetail;

    private static final String ARG_RUN = "arg_run";

    public static RunDetailFragment newInstance(Run run) {
        RunDetailFragment fragment = new RunDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RUN, run);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View buttonBack = view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showHome();
            } else {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        TextView textDetailDistance = view.findViewById(R.id.textDetailDistance);
        TextView textDetailDuration = view.findViewById(R.id.textDetailDuration);
        TextView textDetailPace = view.findViewById(R.id.textDetailPace);
        TextView textDetailDate = view.findViewById(R.id.textDetailDate);
        mapDetail = view.findViewById(R.id.mapDetail);
        OsmMapHelper.configure(requireContext(), mapDetail);
        OsmMapHelper.updateLocation(mapDetail, null, null, true);

        Run run = getRunArgument();
        if (run == null) {
            return;
        }
        textDetailDistance.setText(String.format(Locale.US, "%.2f km", run.getDistanceKm()));
        textDetailDuration.setText(run.getDuration() == null ? "--:--" : run.getDuration());
        textDetailPace.setText(run.getPace() == null ? "-- min/km" : run.getPace());
        textDetailDate.setText(run.getDate() == null ? "" : run.getDate());
        if (run.getRoutePoints() != null && !run.getRoutePoints().isEmpty()) {
            OsmMapHelper.updateRoute(mapDetail, run.getRoutePoints(), true);
        } else {
            OsmMapHelper.updateLocation(mapDetail, run.getLatitude(), run.getLongitude(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapDetail != null) {
            mapDetail.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapDetail != null) {
            mapDetail.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mapDetail != null) {
            mapDetail.onDetach();
            mapDetail = null;
        }
        super.onDestroyView();
    }

    private Run getRunArgument() {
        Bundle args = getArguments();
        if (args == null) {
            return null;
        }
        Object run = args.getSerializable(ARG_RUN);
        if (run instanceof Run) {
            return (Run) run;
        }
        return null;
    }
}
