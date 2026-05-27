package com.pascalhain.runconquer.data.model;

import java.util.List;
import java.util.Locale;

public class SeasonStats {

    private final int totalRuns;
    private final double totalDistanceKm;
    private final double bestDistanceKm;
    private final String averagePace;

    public SeasonStats(int totalRuns, double totalDistanceKm, double bestDistanceKm,
                       String averagePace) {
        this.totalRuns = totalRuns;
        this.totalDistanceKm = totalDistanceKm;
        this.bestDistanceKm = bestDistanceKm;
        this.averagePace = averagePace;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public double getBestDistanceKm() {
        return bestDistanceKm;
    }

    public String getAveragePace() {
        return averagePace;
    }

    public static SeasonStats fromRuns(List<Run> runs) {
        if (runs == null || runs.isEmpty()) {
            return new SeasonStats(0, 0.0, 0.0, "-- min/km");
        }
        int count = 0;
        double totalDistance = 0.0;
        double bestDistance = 0.0;
        long totalSeconds = 0L;

        for (Run run : runs) {
            if (run == null) {
                continue;
            }
            count++;
            totalDistance += run.getDistanceKm();
            bestDistance = Math.max(bestDistance, run.getDistanceKm());
            totalSeconds += parseDurationSeconds(run.getDuration());
        }

        String avgPace = formatPace(totalSeconds, totalDistance);
        return new SeasonStats(count, totalDistance, bestDistance, avgPace);
    }

    private static long parseDurationSeconds(String duration) {
        if (duration == null || duration.isEmpty()) {
            return 0L;
        }
        String[] parts = duration.split(":");
        try {
            if (parts.length == 2) {
                long minutes = Long.parseLong(parts[0]);
                long seconds = Long.parseLong(parts[1]);
                return minutes * 60L + seconds;
            }
            if (parts.length == 3) {
                long hours = Long.parseLong(parts[0]);
                long minutes = Long.parseLong(parts[1]);
                long seconds = Long.parseLong(parts[2]);
                return hours * 3600L + minutes * 60L + seconds;
            }
        } catch (NumberFormatException ignored) {
            return 0L;
        }
        return 0L;
    }

    private static String formatPace(long totalSeconds, double totalDistanceKm) {
        if (totalDistanceKm <= 0.0 || totalSeconds <= 0L) {
            return "-- min/km";
        }
        long paceSeconds = Math.round(totalSeconds / totalDistanceKm);
        long minutes = paceSeconds / 60L;
        long seconds = paceSeconds % 60L;
        return String.format(Locale.US, "%02d:%02d min/km", minutes, seconds);
    }
}
