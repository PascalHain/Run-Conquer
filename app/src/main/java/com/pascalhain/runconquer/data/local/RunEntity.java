package com.pascalhain.runconquer.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "runs")
public class RunEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String userId;
    private String dateLabel;
    private String locationName;
    private long startedAtMs;
    private long endedAtMs;
    private int durationSeconds;
    private double distanceKm;
    private String pace;
    private int calories;
    private double avgSpeedMps;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String territoryId;
    private String duelResult;
    private String opponentUserId;
    private int conquerScore;
    private int earnedXp;

    public RunEntity(@NonNull String id, String userId, String dateLabel, String locationName,
                     long startedAtMs, long endedAtMs, int durationSeconds, double distanceKm,
                     String pace, int calories, double avgSpeedMps, double startLatitude,
                     double startLongitude, double endLatitude, double endLongitude,
                     String territoryId, String duelResult, String opponentUserId,
                     int conquerScore, int earnedXp) {
        this.id = id;
        this.userId = userId;
        this.dateLabel = dateLabel;
        this.locationName = locationName;
        this.startedAtMs = startedAtMs;
        this.endedAtMs = endedAtMs;
        this.durationSeconds = durationSeconds;
        this.distanceKm = distanceKm;
        this.pace = pace;
        this.calories = calories;
        this.avgSpeedMps = avgSpeedMps;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.territoryId = territoryId;
        this.duelResult = duelResult;
        this.opponentUserId = opponentUserId;
        this.conquerScore = conquerScore;
        this.earnedXp = earnedXp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDateLabel() {
        return dateLabel;
    }

    public String getLocationName() {
        return locationName;
    }

    public long getStartedAtMs() {
        return startedAtMs;
    }

    public long getEndedAtMs() {
        return endedAtMs;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public String getPace() {
        return pace;
    }

    public int getCalories() {
        return calories;
    }

    public double getAvgSpeedMps() {
        return avgSpeedMps;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public String getTerritoryId() {
        return territoryId;
    }

    public String getDuelResult() {
        return duelResult;
    }

    public String getOpponentUserId() {
        return opponentUserId;
    }

    public int getConquerScore() {
        return conquerScore;
    }

    public int getEarnedXp() {
        return earnedXp;
    }
}

