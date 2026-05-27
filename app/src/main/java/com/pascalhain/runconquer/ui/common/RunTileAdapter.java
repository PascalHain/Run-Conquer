package com.pascalhain.runconquer.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pascalhain.runconquer.R;
import com.pascalhain.runconquer.data.model.Run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RunTileAdapter extends RecyclerView.Adapter<RunTileAdapter.RunTileViewHolder> {

    public interface OnRunClickListener {
        void onRunClicked(Run run);
    }

    public interface RunTextProvider {
        String getText(Run run, int position);
    }

    private List<Run> runs = Collections.emptyList();
    private int maxItems = Integer.MAX_VALUE;
    private OnRunClickListener clickListener;
    private String titleOverride;
    private String subtitleOverride;
    private RunTextProvider titleProvider;
    private RunTextProvider subtitleProvider;

    @NonNull
    @Override
    public RunTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_run_tile, parent, false);
        return new RunTileViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RunTileViewHolder holder, int position) {
        Run run = runs.get(position);
        holder.bind(run, position, titleOverride, subtitleOverride, titleProvider, subtitleProvider);
    }

    @Override
    public int getItemCount() {
        return Math.min(runs.size(), maxItems);
    }

    public void submitRuns(List<Run> runs) {
        if (runs == null) {
            this.runs = Collections.emptyList();
        } else {
            this.runs = new ArrayList<>(runs);
        }
        notifyDataSetChanged();
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = Math.max(0, maxItems);
        notifyDataSetChanged();
    }

    public void setOnRunClickListener(OnRunClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    public void setTitleOverride(String titleOverride) {
        this.titleOverride = titleOverride;
        notifyDataSetChanged();
    }

    public void setSubtitleOverride(String subtitleOverride) {
        this.subtitleOverride = subtitleOverride;
        notifyDataSetChanged();
    }

    public void setTitleProvider(RunTextProvider titleProvider) {
        this.titleProvider = titleProvider;
        notifyDataSetChanged();
    }

    public void setSubtitleProvider(RunTextProvider subtitleProvider) {
        this.subtitleProvider = subtitleProvider;
        notifyDataSetChanged();
    }

    static class RunTileViewHolder extends RecyclerView.ViewHolder {

        private final View mapRunTilePlaceholder;
        private final TextView textRunTitle;
        private final TextView textRunSubtitle;
        private final TextView textRunMeta;
        private final TextView textRunDistance;
        private final TextView textRunDuration;
        private final TextView textRunPace;
        private final OnRunClickListener clickListener;
        private Run boundRun;

        RunTileViewHolder(@NonNull View itemView, OnRunClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            mapRunTilePlaceholder = itemView.findViewById(R.id.mapRunTilePlaceholder);
            textRunTitle = itemView.findViewById(R.id.textRunTitle);
            textRunSubtitle = itemView.findViewById(R.id.textRunSubtitle);
            textRunMeta = itemView.findViewById(R.id.textRunMeta);
            textRunDistance = itemView.findViewById(R.id.textRunDistance);
            textRunDuration = itemView.findViewById(R.id.textRunDuration);
            textRunPace = itemView.findViewById(R.id.textRunPace);

            itemView.setOnClickListener(v -> {
                if (this.clickListener != null && boundRun != null) {
                    this.clickListener.onRunClicked(boundRun);
                }
            });
        }

        void bind(Run run, int position, String titleOverride, String subtitleOverride,
                  RunTextProvider titleProvider, RunTextProvider subtitleProvider) {
            if (run == null) {
                return;
            }
            boundRun = run;
            String location = run.getLocationName() == null ? "" : run.getLocationName();
            String date = run.getDate() == null ? "" : run.getDate();
            String providedTitle = titleProvider == null ? null : titleProvider.getText(run, position);
            String providedSubtitle = subtitleProvider == null ? null : subtitleProvider.getText(run, position);
            String title = providedTitle != null && !providedTitle.isEmpty()
                    ? providedTitle
                    : (titleOverride == null || titleOverride.isEmpty()
                    ? (location.isEmpty() ? "Run" : location)
                    : titleOverride);
            String meta = date.isEmpty() ? location : (location.isEmpty() ? date : date + " · " + location);
            textRunTitle.setText(title);
            String subtitle = providedSubtitle != null && !providedSubtitle.isEmpty()
                    ? providedSubtitle
                    : subtitleOverride;
            if (subtitle == null || subtitle.isEmpty()) {
                textRunSubtitle.setVisibility(View.GONE);
            } else {
                textRunSubtitle.setVisibility(View.VISIBLE);
                textRunSubtitle.setText(subtitle);
            }
            textRunMeta.setText(meta);
            textRunDistance.setText(String.format(Locale.US, "%.1f km", run.getDistanceKm()));
            textRunDuration.setText(run.getDuration() == null ? "--:--" : run.getDuration());
            textRunPace.setText(run.getPace() == null ? "-- min/km" : run.getPace());
            if (mapRunTilePlaceholder != null) {
                mapRunTilePlaceholder.setVisibility(View.VISIBLE);
            }
        }
    }
}
