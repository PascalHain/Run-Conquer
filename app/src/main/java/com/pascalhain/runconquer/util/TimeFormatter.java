package com.pascalhain.runconquer.util;

public final class TimeFormatter {

    private TimeFormatter() {
    }

    public static String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
