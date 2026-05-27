package com.pascalhain.runconquer.util;

public final class PaceCalculator {

    private PaceCalculator() {
    }

    public static String calculatePace(double distanceKm, int durationSeconds) {
        if (distanceKm <= 0) {
            return "-";
        }
        double paceSeconds = durationSeconds / distanceKm;
        int minutes = (int) (paceSeconds / 60);
        int seconds = (int) (paceSeconds % 60);
        return String.format("%02d:%02d min/km", minutes, seconds);
    }
}
